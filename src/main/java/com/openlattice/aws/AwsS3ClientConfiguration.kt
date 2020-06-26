package com.openlattice.aws

import com.fasterxml.jackson.annotation.JsonProperty

data class AwsS3ClientConfiguration(
        @JsonProperty("bucket") val bucket: String,
        @JsonProperty("regionName") val regionName: String,
        @JsonProperty("accessKeyId") val accessKeyId: String,
        @JsonProperty("secretAccessKey") val secretAccessKey: String
)