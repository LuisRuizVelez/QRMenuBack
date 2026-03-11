package firebase

import bases.BaseModel

class FBProject extends BaseModel implements Serializable {
    String name
    String configurationFileName
    String bucketName
    String defaultDb
    String projectName
    String translateLocation
    String glossaryPath
    Boolean useRealtime
    Boolean useFirestore

    static hasMany = [
        databases: FBDatabase
    ]

    static mapping = {
        table 'fb_project'
        id column: 'id_fb_project', generator: 'uuid'
    }

    static constraints = {
        name                    nullable: true, blank: true, unique: true
        configurationFileName   nullable: true, blank: true
        bucketName              nullable: true, blank: true
        defaultDb               nullable: true, blank: true
        projectName             nullable: true, blank: true
        translateLocation       nullable: true, blank: true
        glossaryPath            nullable: true, blank: true
        useRealtime             nullable: true, blank: true
        useFirestore            nullable: true, blank: true
    }

    def formGlossaryPath = { String fileName ->
        glossaryPath.replace('{fileName}',fileName)
    }

    def toJsonForm =  {
        [
                id: id,
                name: name,
                configurationFileName: configurationFileName,
                bucketName: bucketName,
                defaultDb: defaultDb,
                projectName: projectName,
                translateLocation: translateLocation,
                glossaryPath: glossaryPath,
                useRealtime: useRealtime,
                useFirestore: useFirestore,
        ]
    }

    def toBasicForm = {
        [
                id: id,
                name: name
        ]
    }
}






