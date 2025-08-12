package org.wso2.carbon.apimgt.rest.api.admin.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.xml.bind.annotation.*;
import org.wso2.carbon.apimgt.rest.api.common.annotations.Scope;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.Valid;



public class ModelProviderDTO   {
  
    private List<String> models = new ArrayList<String>();
    private String name = null;

  /**
   **/
  public ModelProviderDTO models(List<String> models) {
    this.models = models;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("models")
  @NotNull
  public List<String> getModels() {
    return models;
  }
  public void setModels(List<String> models) {
    this.models = models;
  }

  /**
   **/
  public ModelProviderDTO name(String name) {
    this.name = name;
    return this;
  }

  
  @ApiModelProperty(example = "OpenAI", required = true, value = "")
  @JsonProperty("name")
  @NotNull
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelProviderDTO modelProvider = (ModelProviderDTO) o;
    return Objects.equals(models, modelProvider.models) &&
        Objects.equals(name, modelProvider.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(models, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelProviderDTO {\n");
    
    sb.append("    models: ").append(toIndentedString(models)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

