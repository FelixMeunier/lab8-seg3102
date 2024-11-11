package seg3x02.employeeGql.resolvers

import graphql.GraphQL
import org.springframework.graphql.GraphQlService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import seg3x02.employeeGql.entity.Employee
import seg3x02.employeeGql.service.EmployeeService

@Controller
@RequestMapping("/graphql")
class EmployeesResolver(
    private val graphQlService: GraphQlService,
    private val employeeService: EmployeeService
) {

    // Query to get all employees
    @PostMapping
    fun getEmployees(@RequestBody request: Map<String, Any>): ResponseEntity<Any> {
        val query = request["query"]?.toString()
        if (query != null) {
            val executionInput = GraphQL.newExecutionInput()
                .query(query)
                .build()
            val result = graphQlService.execute(executionInput)
            return ResponseEntity.ok(result)
        } else {
            return ResponseEntity.badRequest().body("Query parameter missing.")
        }
    }

    // Mutation to add an employee
    @PostMapping("/addEmployee")
    fun addEmployee(@RequestBody employeeData: Map<String, Any>): ResponseEntity<Any> {
        val name = employeeData["name"]?.toString()
        val dateOfBirth = employeeData["dateOfBirth"]?.toString()
        val city = employeeData["city"]?.toString()
        val salary = employeeData["salary"]?.toString()?.toFloat()
        val gender = employeeData["gender"]?.toString()
        val email = employeeData["email"]?.toString()

        if (name != null && dateOfBirth != null && city != null && salary != null) {
            val newEmployee = employeeService.addEmployee(name, dateOfBirth, city, salary, gender, email)
            return ResponseEntity.ok(newEmployee)
        } else {
            return ResponseEntity.badRequest().body("Missing required fields.")
        }
    }
}

