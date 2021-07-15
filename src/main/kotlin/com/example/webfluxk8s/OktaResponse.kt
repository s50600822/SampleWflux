package com.example.webfluxk8s

data class OktaResponse(
    val authorization_endpoint: String,
    val claims_supported: List<String>,
    val code_challenge_methods_supported: List<String>,
    val end_session_endpoint: String,
    val grant_types_supported: List<String>,
    val introspection_endpoint: String,
    val introspection_endpoint_auth_methods_supported: List<String>,
    val issuer: String,
    val jwks_uri: String,
    val registration_endpoint: String,
    val request_object_signing_alg_values_supported: List<String>,
    val request_parameter_supported: Boolean,
    val response_modes_supported: List<String>,
    val response_types_supported: List<String>,
    val revocation_endpoint: String,
    val revocation_endpoint_auth_methods_supported: List<String>,
    val scopes_supported: List<String>,
    val subject_types_supported: List<String>,
    val token_endpoint: String,
    val token_endpoint_auth_methods_supported: List<String>
)
