package org.wso2.carbon.apimgt.rest.api.publisher.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;


import io.swagger.annotations.*;
import java.util.Objects;

import javax.xml.bind.annotation.*;
import org.wso2.carbon.apimgt.rest.api.common.annotations.Scope;
import com.fasterxml.jackson.annotation.JsonCreator;

import javax.validation.Valid;



public class BackendOperationDTO   {
  
    private String target = null;

    @XmlType(name="VerbEnum")
    @XmlEnum(String.class)
    public enum VerbEnum {
        GET("GET"),
        POST("POST"),
        PUT("PUT"),
        DELETE("DELETE"),
        PATCH("PATCH"),
        HEAD("HEAD"),
        OPTIONS("OPTIONS");
        private String value;

        VerbEnum (String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static VerbEnum fromValue(String v) {
            for (VerbEnum b : VerbEnum.values()) {
                if (String.valueOf(b.value).equals(v)) {
                    return b;
                }
            }
return null;
        }
    }
    private VerbEnum verb = null;

  /**
   * Backend target path
   **/
  public BackendOperationDTO target(String target) {
    this.target = target;
    return this;
  }

  
  @ApiModelProperty(example = "/order/{orderId}", value = "Backend target path")
  @JsonProperty("target")
  public String getTarget() {
    return target;
  }
  public void setTarget(String target) {
    this.target = target;
  }

  /**
   * Backend target method
   **/
  public BackendOperationDTO verb(VerbEnum verb) {
    this.verb = verb;
    return this;
  }

  
  @ApiModelProperty(example = "POST", value = "Backend target method")
  @JsonProperty("verb")
  public VerbEnum getVerb() {
    return verb;
  }
  public void setVerb(VerbEnum verb) {
    this.verb = verb;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BackendOperationDTO backendOperation = (BackendOperationDTO) o;
    return Objects.equals(target, backendOperation.target) &&
        Objects.equals(verb, backendOperation.verb);
  }

  @Override
  public int hashCode() {
    return Objects.hash(target, verb);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BackendOperationDTO {\n");
    
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
    sb.append("    verb: ").append(toIndentedString(verb)).append("\n");
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

