package com.app.web.utis;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by Sergey on 08.09.2017.
 */
@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException e) {
        if (e instanceof WebApplicationException) {
            return ((WebApplicationException) e).getResponse();
        } else if (e instanceof  IllegalArgumentException){
            return Response.status(Response.Status.BAD_REQUEST)
                    .build();
        }
        // LOGGER.log(Level.WARNING, "RuntimeException occurred", e); TODO add logger

        System.out.println("Exception occurred " + e);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }
}
