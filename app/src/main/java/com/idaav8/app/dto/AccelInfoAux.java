package com.idaav8.app.dto;

public record AccelInfoAux(
    String accelName, 
    String ownerEmail, 
    String deploymentStatus, 
    String dns,
    String ipAddress) {

        public boolean hasOwner() {

            return ownerEmail != null

            && ownerEmail.contains("@");

        }

    }

