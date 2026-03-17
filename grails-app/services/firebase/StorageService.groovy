package firebase

import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit
import com.google.cloud.WriteChannel
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.Storage
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.StorageOptions
import com.google.auth.oauth2.GoogleCredentials

import exeptions.MissingRequirementException
import grails.gorm.transactions.Transactional
import org.springframework.web.multipart.MultipartFile


import bases.BaseService

@Transactional
class StorageService extends BaseService {

    /**
     * Uploads a file to Google Cloud Storage.
     *
     * @param item An object containing metadata about the file to be uploaded.
     *             The `item` must have a `getClassName()` method to retrieve its class name.
     * @param multipartFile The file to be uploaded, provided as a `MultipartFile`.
     * @return `true` if the file was successfully uploaded, otherwise throws an exception.
     * @throws MissingRequirementException If the Firebase path is not found.
     * @throws Exception If any other error occurs during the upload process.
     */
    Map uploadFile(def item, MultipartFile multipartFile, Boolean isThumb = false) {
        try {
            String className = item.getClassName()
            FBChildPath path = FBChildPath.findByComponentName(className)

            if (path == null || path?.url?.isEmpty())
                throw new MissingRequirementException("Firebase path not found")

            String fileName = "${className}_${item?.id}_${System.currentTimeMillis()}"
            String absolutePath = pathManager path.url, fileName
            String configurationFileName = path?.fbDatabase?.project?.configurationFileName
            String bucketName = path?.fbDatabase?.project?.bucketName
            Storage storage = getStorage configurationFileName

            createFile absolutePath, bucketName, multipartFile, storage

            return [
                    name: fileName,
                    url: getMediaUrl(bucketName, configurationFileName, absolutePath),
                    path: absolutePath,
                    isThumb: isThumb
            ]
        } catch (Exception ex) {
            ex.printStackTrace()

            return null
        }
    }


    /**
     * Deletes a file from Google Cloud Storage.
     *
     * @param item An object containing metadata about the file to be deleted.
     *             The `item` must have a `getClassName()` method to retrieve its class name.
     * @param fileName The name of the file to be deleted.
     * @return `true` if the file was successfully deleted, otherwise returns `false`.
     * @throws MissingRequirementException If the Firebase path is not found.
     * @throws Exception If any other error occurs during the deletion process.
     */
    boolean deleteFile(def item, String fileName) {
        try {
            String className = item.getClassName()
            FBChildPath path = FBChildPath.findByComponentName(className)

            if (path == null || path?.url?.isEmpty())
                throw new MissingRequirementException("Firebase path not found")

            String absolutePath = pathManager path.url, fileName
            String configurationFileName = path?.fbDatabase?.project?.configurationFileName
            String bucketName = path?.fbDatabase?.project?.bucketName
            Storage storage = getStorage configurationFileName

            removeFile absolutePath, bucketName, storage

            return true
        } catch (Exception ex) {
            ex.printStackTrace()
            return false
        }
    }



    /**
     * Creates a file in the specified Google Cloud Storage bucket.
     *
     * @param absolutePath The path within the bucket where the file will be stored.
     * @param bucketName The name of the Google Cloud Storage bucket.
     * @param multipartFile The file to be uploaded, provided as a `MultipartFile`.
     * @param storage The `Storage` instance used to interact with Google Cloud Storage.
     * @return `true` if the file was successfully created, `false` otherwise.
     */
    private static boolean createFile(String absolutePath, String bucketName, MultipartFile multipartFile, Storage storage){
        BlobId blobId = BlobId.of(bucketName, absolutePath)
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(multipartFile.getContentType()).build()

        try (WriteChannel writer = storage.writer(blobInfo)) {
            byte[] content = multipartFile.getBytes() // Obtener los bytes del archivo
            writer.write(ByteBuffer.wrap(content)) // Escribir los bytes en el canal
        } catch (IOException ex) {
            ex.printStackTrace()
            return false
        }

        return true
    }


    /**
     * Deletes a file from Google Cloud Storage.
     *
     * @param absolutePath The path within the bucket where the file is stored.
     * @param bucketName The name of the Google Cloud Storage bucket.
     * @param storage The `Storage` instance used to interact with Google Cloud Storage.
     * @return `true` if the file was successfully deleted, `false` otherwise.
     */
    private static boolean removeFile(String absolutePath, String bucketName, Storage storage) {
        BlobId blobId = BlobId.of(bucketName, absolutePath)

        try {
            return storage.delete(blobId)
        } catch (Exception ex) {
            ex.printStackTrace()
            return false
        }
    }




    /**
     * Obtains a Google Cloud Storage instance using the provided service account file.
     *
     * @param storageName The name of the service account file to load from the classpath.
     * @return A `Storage` instance configured with the provided credentials.
     * @throws Exception If an error occurs while loading the credentials or initializing the storage.
     */
    private Storage getStorage(String storageName) {
        try {
            InputStream serviceAccount = this.class.classLoader.getResourceAsStream(storageName)
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount)
            Storage storage = StorageOptions.newBuilder().setCredentials( credentials ).build().getService()

            return storage
        } catch (Exception ex) {
            ex.printStackTrace()
        }

        return null
    }


    /**
     * Constructs a new path by modifying the given path based on the provided parameters.
     *
     * @param path The base path to be modified.
     * @param fileName (Optional) The name of the file to append to the path.
     * @param adds (Optional) A map containing additional parameters:
     *             - 'start': A string to prepend to the path.
     *             - 'end': A string to append to the path.
     * @return The modified path as a `String`.
     */
    private static String pathManager(String path, String fileName=null, Map<String, String> adds = [:]) {
        String newPath = path

        if(path.startsWith("/"))
            newPath = path.substring(1)

        if(adds['start'] != null)
            newPath = adds['start'] + newPath

        if(adds['end'] != null)
            newPath = newPath + adds['end']

        if(fileName != null)
            newPath = newPath + "/${fileName}"

        return newPath
    }


    /**
     * Retrieves the public URL of a file stored in Google Cloud Storage.
     *
     * This method generates the public URL of a file stored in a Google Cloud Storage bucket
     * using the bucket name, the file's absolute path, and the provided service account credentials.
     *
     * @param bucketName The name of the Google Cloud Storage bucket where the file is located.
     * @param storageName The name of the service account credentials file used to access the bucket.
     * @param absolutePath The full path of the file within the bucket.
     * @return The public URL of the file as a `String`, or an empty string if the file is not found or an error occurs.
     */
    private String getMediaUrl(String bucketName, String storageName, String absolutePath) {
        try {
            Storage storage = getStorage(storageName)
            BlobId blobId = BlobId.of(bucketName, absolutePath)
            BlobInfo blobInfo = storage.get(blobId)

            // Attempt to generate a signed URL for the blob
            URL signedUrl = storage.signUrl(blobInfo, 7, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature())

            if (blobInfo != null) {


                String mediaUrl = "https://firebasestorage.googleapis.com/v0/b/qrmenu-bb916.firebasestorage.app/o/core%2Fmenu%2FMenu_402881569a56fd2b019a57077d980008_1772678188490?alt=media&token=3fb3f9fb-b2f2-4f3a-aefc-fdf48a0de8b4"




                return "https://storage.googleapis.com/${bucketName}/${absolutePath}"
            } else {
                println "File not found in bucket: ${bucketName}, path: ${absolutePath}"
                return ""
            }
        } catch (Exception e) {
            println "Error getting media URL: \n$e"
            return ""
        }
    }
}
