package org.wso2.carbon.apimgt.internal.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.wso2.carbon.apimgt.internal.service.dto.APIOperationMappingDTO;
import org.wso2.carbon.apimgt.internal.service.dto.BackendOperationMappingDTO;
import org.wso2.carbon.apimgt.internal.service.dto.OperationPolicyDTO;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.xml.bind.annotation.*;
import org.wso2.carbon.apimgt.rest.api.common.annotations.Scope;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.Valid;



public class URLMappingDTO   {
  
    private String authScheme = null;
    private String throttlingPolicy = null;
    private String httpMethod = null;
    private String urlPattern = null;
    private List<String> scopes = new ArrayList<>();
    private String description = null;
    private String schemaDefinition = null;
    private BackendOperationMappingDTO backendOperationMapping = null;
    private APIOperationMappingDTO apiOperationMapping = null;
    private List<OperationPolicyDTO> operationPolicies = new ArrayList<>();

  /**
   **/
  public URLMappingDTO authScheme(String authScheme) {
    this.authScheme = authScheme;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("authScheme")
  public String getAuthScheme() {
    return authScheme;
  }
  public void setAuthScheme(String authScheme) {
    this.authScheme = authScheme;
  }

  /**
   **/
  public URLMappingDTO throttlingPolicy(String throttlingPolicy) {
    this.throttlingPolicy = throttlingPolicy;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("throttlingPolicy")
  public String getThrottlingPolicy() {
    return throttlingPolicy;
  }
  public void setThrottlingPolicy(String throttlingPolicy) {
    this.throttlingPolicy = throttlingPolicy;
  }

  /**
   **/
  public URLMappingDTO httpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("httpMethod")
  public String getHttpMethod() {
    return httpMethod;
  }
  public void setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
  }

  /**
   **/
  public URLMappingDTO urlPattern(String urlPattern) {
    this.urlPattern = urlPattern;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("urlPattern")
  public String getUrlPattern() {
    return urlPattern;
  }
  public void setUrlPattern(String urlPattern) {
    this.urlPattern = urlPattern;
  }

  /**
   **/
  public URLMappingDTO scopes(List<String> scopes) {
    this.scopes = scopes;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("scopes")
  public List<String> getScopes() {
    return scopes;
  }
  public void setScopes(List<String> scopes) {
    this.scopes = scopes;
  }

  /**
   **/
  public URLMappingDTO description(String description) {
    this.description = description;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   **/
  public URLMappingDTO schemaDefinition(String schemaDefinition) {
    this.schemaDefinition = schemaDefinition;
    return this;
  }

  
  @ApiModelProperty(value = "")
  @JsonProperty("schemaDefinition")
  public String getSchemaDefinition() {
    return schemaDefinition;
  }
  public void setSchemaDefinition(String schemaDefinition) {
    this.schemaDefinition = schemaDefinition;
  }

  /**
   **/
  public URLMappingDTO backendOperationMapping(BackendOperationMappingDTO backendOperationMapping) {
    this.backendOperationMapping = backendOperationMapping;
    return this;
  }

  
  @ApiModelProperty(value = "")
      @Valid
  @JsonProperty("backendOperationMapping")
  public BackendOperationMappingDTO getBackendOperationMapping() {
    return backendOperationMapping;
  }
  public void setBackendOperationMapping(BackendOperationMappingDTO backendOperationMapping) {
    this.backendOperationMapping = backendOperationMapping;
  }

  /**
   **/
  public URLMappingDTO apiOperationMapping(APIOperationMappingDTO apiOperationMapping) {
    this.apiOperationMapping = apiOperationMapping;
    return this;
  }

  
  @ApiModelProperty(value = "")
      @Valid
  @JsonProperty("apiOperationMapping")
  public APIOperationMappingDTO getApiOperationMapping() {
    return apiOperationMapping;
  }
  public void setApiOperationMapping(APIOperationMappingDTO apiOperationMapping) {
    this.apiOperationMapping = apiOperationMapping;
  }

  /**
   **/
  public URLMappingDTO operationPolicies(List<OperationPolicyDTO> operationPolicies) {
    this.operationPolicies = operationPolicies;
    return this;
  }

  
  @ApiModelProperty(value = "")
      @Valid
  @JsonProperty("operationPolicies")
  public List<OperationPolicyDTO> getOperationPolicies() {
    return operationPolicies;
  }
  public void setOperationPolicies(List<OperationPolicyDTO> operationPolicies) {
    this.operationPolicies = operationPolicies;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    URLMappingDTO urLMapping = (URLMappingDTO) o;
    return Objects.equals(authScheme, urLMapping.authScheme) &&
        Objects.equals(throttlingPolicy, urLMapping.throttlingPolicy) &&
        Objects.equals(httpMethod, urLMapping.httpMethod) &&
        Objects.equals(urlPattern, urLMapping.urlPattern) &&
        Objects.equals(scopes, urLMapping.scopes) &&
        Objects.equals(description, urLMapping.description) &&
        Objects.equals(schemaDefinition, urLMapping.schemaDefinition) &&
        Objects.equals(backendOperationMapping, urLMapping.backendOperationMapping) &&
        Objects.equals(apiOperationMapping, urLMapping.apiOperationMapping) &&
        Objects.equals(operationPolicies, urLMapping.operationPolicies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authScheme, throttlingPolicy, httpMethod, urlPattern, scopes, description, schemaDefinition, backendOperationMapping, apiOperationMapping, operationPolicies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class URLMappingDTO {\n");
    
    sb.append("    authScheme: ").append(toIndentedString(authScheme)).append("\n");
    sb.append("    throttlingPolicy: ").append(toIndentedString(throttlingPolicy)).append("\n");
    sb.append("    httpMethod: ").append(toIndentedString(httpMethod)).append("\n");
    sb.append("    urlPattern: ").append(toIndentedString(urlPattern)).append("\n");
    sb.append("    scopes: ").append(toIndentedString(scopes)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    schemaDefinition: ").append(toIndentedString(schemaDefinition)).append("\n");
    sb.append("    backendOperationMapping: ").append(toIndentedString(backendOperationMapping)).append("\n");
    sb.append("    apiOperationMapping: ").append(toIndentedString(apiOperationMapping)).append("\n");
    sb.append("    operationPolicies: ").append(toIndentedString(operationPolicies)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

