package webservice


import com.google.gson.Gson
import helpers.Database
import helpers.Logger
import model.Event
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
            SpringApplication.run(WebServiceApplication::class.java)
        }
    }

    @PostMapping("/log")
    fun log(@RequestBody requestBody: String): ResponseEntity<Any> {
        val event = Gson().fromJson(requestBody, Event::class.java)
        Logger.log(event)
        return ResponseEntity(event, HttpStatus.OK)
    }
}
