package com.vitaly.dlmanager.dto;

public record FileResponse(String name, String uploadId, String path, String type, String eTag) {}

