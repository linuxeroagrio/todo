package io.linuxeroagrio;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;

import io.linuxeroagrio.todoapi.model.TodoItem;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class TodoItemResourceTest {

    @Test
    public void testListTodoItems() {
        Collection<TodoItem> todoItems=given()
        .when().get("/todoitems")
        .then()
           .statusCode(Response.Status.OK.getStatusCode())
           .extract().body().as(Collection.class);

        assertThat(todoItems).size().isGreaterThanOrEqualTo(0);
    }

    @Test
    public void testCRUD(){
        given()
          .body("{\"taskname\":\"TestTask\",\"status\":true}")
          .contentType(ContentType.JSON)
          .when()
          .post("/todoitems")
          .then()
          .statusCode(Response.Status.CREATED.getStatusCode());
        
        TodoItem createdTodoItem=given()
        .when().get("/todoitems/{id}","1")
        .then()
           .statusCode(Response.Status.OK.getStatusCode())
           .extract().body().as(TodoItem.class); 
        assertThat(createdTodoItem.id).isEqualTo(1);
        assertThat(createdTodoItem.taskname).isEqualTo("TestTask");
        assertThat(createdTodoItem.status).isEqualTo(true);

        
        given()
          .body("{\"taskname\":\"TestTask\",\"status\":false}")
          .contentType(ContentType.JSON)
          .when()
          .put("/todoitems/{item}","1")
          .then()
          .statusCode(Response.Status.ACCEPTED.getStatusCode());
        TodoItem updatedTodoItem=given()
          .when().get("/todoitems/{id}","1")
          .then()
             .statusCode(Response.Status.OK.getStatusCode())
             .extract().body().as(TodoItem.class); 
          assertThat(updatedTodoItem.id).isEqualTo(1);
          assertThat(updatedTodoItem.taskname).isEqualTo("TestTask");
          assertThat(updatedTodoItem.status).isEqualTo(false);
        
        given().when().delete("/todoitems/{id}","1")
          .then()
          .statusCode(Response.Status.ACCEPTED.getStatusCode());

        given().when().get("/todoitems/{id}","1")
          .then()
          .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }
}