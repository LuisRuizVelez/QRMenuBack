package qrmenuback

class UrlMappings {

    static mappings = {

        post    "/api/$controller/search(.$format)?"(action:"search") // Mapeo al método de búsqueda universal
        post    "/api/$controller(.$format)?"(action:"save")
        put     "/api/$controller/$id(.$format)?"(action:"update")
        delete  "/api/$controller/$id(.$format)?"(action:"delete")
        delete  "/api/$controller/$id/$fbDatabase(.$format)?"(action:"delete")
        post    "/api/$controller/getOptions(.$format)?"(action:"getOptions") // Mapeo al método de búsqueda para llenar combos


        // Mapeo de gestión de roles de usuario
        get    "/api/User/getGroupingRole/$username(.$format)?"(controller: "User", action:"getGroupingRole")
        get    "/api/User/getRoles/$userId(.$format)?"(controller: "User", action:"getRoles")
        post    "/api/User/addRole/$userId/$roleId(.$format)?"(controller: "User", action:"addRole")
        delete    "/api/User/removeRole/$userId/$roleId(.$format)?"(controller: "User", action:"removeRole")

        // Mapeo de gestión de secciones por role
        get    "/api/Role/getSections/$roleId(.$format)?"(controller: "Role", action:"getSections")
        post    "/api/Role/addRole/$roleId/$sectionId(.$format)?"(controller: "Role", action:"addSection")
        delete    "/api/Role/removeSection/$roleId/$sectionId(.$format)?"(controller: "Role", action:"removeSection")

        // Mapeo de gestión de secciones por usuario
        get    "/api/Section/getSectionsByUser/$username(.$format)?"(controller: "Section", action:"getSectionsByUser")


        // Mapeos de dish and drinks
        get    "/api/menu/getDishes/$id(.$format)?"(controller: "Menu", action:"getDishes")
        post    "/api/menu/addDish(.$format)?"(controller:"Menu", action:"addDish")
        delete    "/api/menu/removeDish(.$format)?"(controller:"Menu", action:"removeDish")
        get    "/api/menu/getDrinks/$id(.$format)?"(controller: "Menu", action:"getDrinks")
        post    "/api/menu/addDrink(.$format)?"(controller:"Menu", action:"addDrink")
        delete    "/api/menu/removeDrink(.$format)?"(controller:"Menu", action:"removeDrink")


        get "/$controller(.$format)?"(action:"index")
        get "/$controller/$id(.$format)?"(action:"show")
        patch "/$controller/$id(.$format)?"(action:"patch")

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
