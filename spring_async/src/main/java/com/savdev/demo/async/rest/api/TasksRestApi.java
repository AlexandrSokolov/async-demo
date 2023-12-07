package com.savdev.demo.async.rest.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path(TasksRestApi.PATH)
@Produces({MediaType.APPLICATION_JSON})
public interface TasksRestApi {

  String PATH = "/fire/and/wait";

  @GET
  @Path("/async")
  Result fireAsync();

  @GET
  @Path("/async/wrapper")
  Result fireAsyncViaWrapper();
}
