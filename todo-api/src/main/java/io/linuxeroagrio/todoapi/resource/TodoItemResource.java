package io.linuxeroagrio.todoapi.resource;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.jboss.logging.Logger;

import io.linuxeroagrio.todoapi.config.TodoItemConfig;
import io.linuxeroagrio.todoapi.model.TodoItem;
import io.linuxeroagrio.todoapi.model.ValidationGroups;

@Path("todoitems")
@ApplicationScoped
public class TodoItemResource {

    private static final Logger LOGGER=Logger.getLogger(TodoItemResource.class);

    @Inject
    TodoItemConfig todoItemConfig;

    @GET
    @Path("/healthz")
    public String sayHello(){
        return todoItemConfig.message();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<TodoItem> listTodoItems(){
        LOGGER.debug("Todoitem List");
        return TodoItem.listAll();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Response getTodoItemById(@PathParam("id") int id){
        LOGGER.debugf("get TodoItem by id %i", id);
        TodoItem todoItem=TodoItem.findById(id);

        if(todoItem==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(todoItem).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createTodoItem(@Context UriInfo uriInfo, @Valid @ConvertGroup(to=ValidationGroups.Post.class) TodoItem todoItem){
        LOGGER.debugf("create %s",todoItem);
        todoItem.persist();
        UriBuilder path=uriInfo.getAbsolutePathBuilder().path(""+todoItem.id);
        return Response.created(path.build()).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateTodoItem(@PathParam("id") int id, @Valid @ConvertGroup(to=ValidationGroups.Put.class) TodoItem todoItem){
        LOGGER.debugf("update %s",todoItem);
        TodoItem existingTodoItem=TodoItem.findById(id);
        if(existingTodoItem==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existingTodoItem.taskname=todoItem.taskname;
        existingTodoItem.status=todoItem.status;
        existingTodoItem.persist();        
        return Response.accepted(existingTodoItem).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteTodoItem(@PathParam("id") int id){
        LOGGER.debugf("delete by id %i", id);
        TodoItem.delete("id", id);
        return Response.accepted().build();
    }
}
