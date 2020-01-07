package webservice


import CasinoLib.model.Event
import CasinoLib.services.CasinoLibrary
import com.google.gson.GsonBuilder
import helpers.Database
import helpers.Logger
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
open class WebServiceApplication {
    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            Database()
            CasinoLibrary.init("src/main/resources/casinolib.properties")
            SpringApplication.run(WebServiceApplication::class.java)
        }
    }

    @PostMapping("/log")
    fun log(@RequestBody requestBody: String): ResponseEntity<Any> {
        val events = GsonBuilder().create().fromJson(requestBody, Array<Event>::class.java)
        Logger.log(events)
        return ResponseEntity(events, HttpStatus.OK)
    }
}
