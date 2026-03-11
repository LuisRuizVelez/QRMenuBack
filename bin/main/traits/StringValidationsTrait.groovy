package traits

trait StringValidationsTrait {

    /**
     * Verifica si un String es válido según los siguientes criterios:
     * - No es nulo.
     * - No está vacío.
     * - No contiene solo espacios en blanco.
     *
     * @param text El String a validar.
     * @return true si el String es válido, false en caso contrario.
     */
    boolean isValidString(String text){
        if(!text || text.isBlank() )
            return false

        return true
    }

}