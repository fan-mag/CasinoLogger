package webservice


import com.google.gson.Gson
import helpers.Database
import model.Event
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
            //SpringApplication.run(WebServiceApplication::class.java)
            WebServiceApplication().log("""
                {
                    "service":"test1",
                    "message":"test-message1"
                }
            """.trimIndent())
        }
    }

    @PostMapping("/log")
    fun log(@RequestBody requestBody: String): ResponseEntity<Any> {
        val gson = Gson().fromJson(requestBody, Event::class.java)
        println(gson.id)
        return ResponseEntity(gson, HttpStatus.OK)
    }
}
