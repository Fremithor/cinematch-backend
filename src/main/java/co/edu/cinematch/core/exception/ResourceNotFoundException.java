package co.edu.cinematch.core.exception;
public class ResourceNotFoundException extends RuntimeException { public ResourceNotFoundException(String resource, String id) { super(resource + " no encontrado: " + id); } }
