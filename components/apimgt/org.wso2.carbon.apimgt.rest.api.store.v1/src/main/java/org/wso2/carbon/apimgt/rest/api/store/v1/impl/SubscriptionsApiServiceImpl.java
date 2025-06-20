/*
 * Copyright (c) 2019 WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.apimgt.rest.api.store.v1.impl;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.wso2.carbon.apimgt.api.APIConsumer;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.APIMgtAuthorizationFailedException;
import org.wso2.carbon.apimgt.api.ExceptionCodes;
import org.wso2.carbon.apimgt.api.MonetizationException;
import org.wso2.carbon.apimgt.api.SubscriptionAlreadyExistingException;
import org.wso2.carbon.apimgt.api.SubscriptionBlockedException;
import org.wso2.carbon.apimgt.api.WorkflowResponse;
import org.wso2.carbon.apimgt.api.WorkflowStatus;
import org.wso2.carbon.apimgt.api.model.APIIdentifier;
import org.wso2.carbon.apimgt.api.model.ApiTypeWrapper;
import org.wso2.carbon.apimgt.api.model.Application;
import org.wso2.carbon.apimgt.api.model.Monetization;
import org.wso2.carbon.apimgt.api.model.OrganizationInfo;
import org.wso2.carbon.apimgt.api.model.SubscribedAPI;
import org.wso2.carbon.apimgt.api.model.Subscriber;
import org.wso2.carbon.apimgt.api.model.SubscriptionResponse;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;
import org.wso2.carbon.apimgt.impl.workflow.HttpWorkflowResponse;
import org.wso2.carbon.apimgt.rest.api.common.RestApiCommonUtil;
import org.wso2.carbon.apimgt.rest.api.common.RestApiConstants;
import org.wso2.carbon.apimgt.rest.api.store.v1.SubscriptionsApiService;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.AdditionalSubscriptionInfoListDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.APIMonetizationUsageDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.SubscriptionDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.dto.SubscriptionListDTO;
import org.wso2.carbon.apimgt.rest.api.store.v1.mappings.APIMappingUtil;
import org.wso2.carbon.apimgt.rest.api.store.v1.mappings.AdditionalSubscriptionInfoMappingUtil;
import org.wso2.carbon.apimgt.rest.api.store.v1.mappings.SubscriptionMappingUtil;
import org.wso2.carbon.apimgt.rest.api.util.utils.RestAPIStoreUtils;
import org.wso2.carbon.apimgt.rest.api.util.utils.RestApiUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.core.Response;

/**
 * This is the service implementation class for Store subscription related operations
 */
public class SubscriptionsApiServiceImpl implements SubscriptionsApiService {

    private static final Log log = LogFactory.getLog(SubscriptionsApiServiceImpl.class);

    /**
     * Get all subscriptions that are of user or shared subscriptions of the user's group.
     * <p/>
     * If apiId is specified this will return the subscribed applications of that api
     * If application id is specified this will return the api subscriptions of that application
     *
     * @param apiId         api identifier
     * @param applicationId application identifier
     * @param offset        starting index of the subscription list
     * @param limit         max num of subscriptions returned
     * @param ifNoneMatch   If-None-Match header value
     * @return matched subscriptions as a list of SubscriptionDTOs
     */
    @Override
    public Response subscriptionsGet(String apiId, String applicationId, String groupId,
                                     String xWSO2Tenant, Integer offset, Integer limit, String ifNoneMatch,
                                     MessageContext messageContext) {
        String username = RestApiCommonUtil.getLoggedInUsername();
        Subscriber subscriber = new Subscriber(username);
        Set<SubscribedAPI> subscriptions;
        List<SubscribedAPI> subscribedAPIList = new ArrayList<>();

        //pre-processing
        limit = limit != null ? limit : RestApiConstants.PAGINATION_LIMIT_DEFAULT;
        offset = offset != null ? offset : RestApiConstants.PAGINATION_OFFSET_DEFAULT;

        // currently groupId is taken from the user so that groupId coming as a query parameter is not honored.
        // As a improvement, we can check admin privileges of the user and honor groupId.
        groupId = RestApiUtil.getLoggedInUserGroupId();

        try {
            String organization = RestApiUtil.getValidatedOrganization(messageContext);
            APIConsumer apiConsumer = RestApiCommonUtil.getConsumer(username);
            SubscriptionListDTO subscriptionListDTO;
            if (!StringUtils.isEmpty(apiId)) {
                // todo : FIX properly, need to done properly with backend side pagination.
                // todo : getSubscribedIdentifiers() method should NOT be used. Appears to be too slow.

                // This will fail with an authorization failed exception if user does not have permission to access the API
                ApiTypeWrapper apiTypeWrapper = apiConsumer.getAPIorAPIProductByUUID(apiId, organization);

                if (apiTypeWrapper.isAPIProduct()) {
                    subscriptions = apiConsumer.getSubscribedIdentifiers(subscriber,
                            apiTypeWrapper.getApiProduct().getId(), groupId, organization);
                } else {
                    subscriptions = apiConsumer.getSubscribedIdentifiers(subscriber,
                            apiTypeWrapper.getApi().getId(), groupId, organization);
                }

                //sort by application name
                subscribedAPIList.addAll(subscriptions);

                subscribedAPIList.sort(Comparator.comparing(o -> o.getApplication().getName()));

                subscriptionListDTO = SubscriptionMappingUtil
                        .fromSubscriptionListToDTO(subscribedAPIList, limit, offset, organization);

                SubscriptionMappingUtil
                        .setPaginationParams(subscriptionListDTO, apiId, applicationId, groupId, limit, offset,
                                subscribedAPIList.size());

                return Response.ok().entity(subscriptionListDTO).build();
            } else if (!StringUtils.isEmpty(applicationId)) {
                Application application = apiConsumer.getApplicationByUUID(applicationId);

                if (application == null) {
                    RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_APPLICATION, applicationId, log);
                    return null;
                }

                if (!RestAPIStoreUtils.isUserAccessAllowedForApplication(application)) {
                    RestApiUtil.handleAuthorizationFailure(RestApiConstants.RESOURCE_APPLICATION, applicationId, log);
                }

                int total = apiConsumer.getSubscriptionCount(subscriber, application.getName(), groupId);
                subscriptions = apiConsumer.getPaginatedSubscribedAPIsByApplication(application, offset, limit,
                        organization);
                subscribedAPIList.addAll(subscriptions);

                subscriptionListDTO = SubscriptionMappingUtil.fromSubscriptionListToDTO(subscribedAPIList, limit,
                        offset, organization);
                SubscriptionMappingUtil.setPaginationParams(subscriptionListDTO, apiId, applicationId, groupId, limit,
                        offset, total);
                return Response.ok().entity(subscriptionListDTO).build();

            } else {
                //neither apiId nor applicationId is given
                RestApiUtil.handleBadRequest("Either applicationId or apiId should be available", log);
                return null;
            }
        } catch (APIManagementException e) {
            if (RestApiUtil.isDueToAuthorizationFailure(e)) {
                RestApiUtil.handleAuthorizationFailure(RestApiConstants.RESOURCE_API, apiId, log);
            } else if (RestApiUtil.isDueToResourceNotFound(e)) {
                RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_API, apiId, e, log);
            } else {
                RestApiUtil.handleInternalServerError("Error while getting subscriptions of the user " + username, e,
                        log);
            }
        }
        return null;
    }

    /**
     * Creates a new subscriptions with the details specified in the body parameter
     *
     * @param body new subscription details
     * @return newly added subscription as a SubscriptionDTO if successful
     */
    @Override
    public Response subscriptionsPost(SubscriptionDTO body, String xWSO2Tenant, MessageContext messageContext) throws APIManagementException {
        String username = RestApiCommonUtil.getLoggedInUsername();
        APIConsumer apiConsumer;

        try {
            String organization = RestApiUtil.getValidatedOrganization(messageContext);
            String userOrganization = RestApiUtil.getValidatedSubjectOrganization(messageContext);
            apiConsumer = RestApiCommonUtil.getConsumer(username, userOrganization);
            String applicationId = body.getApplicationId();

            //check whether user is permitted to access the API. If the API does not exist,
            // this will throw a APIMgtResourceNotFoundException
            if (body.getApiId() != null) {
                if (!RestAPIStoreUtils.isUserAccessAllowedForAPIByUUID(body.getApiId(), organization)) {
                    RestApiUtil.handleAuthorizationFailure(RestApiConstants.RESOURCE_API, body.getApiId(), log);
                }
            } else {
                RestApiUtil.handleBadRequest(
                        "Request must contain either apiIdentifier or apiProductIdentifier and the relevant type", log);
                return null;
            }

            Application application = apiConsumer.getApplicationByUUID(applicationId);
            if (application == null) {
                //required application not found
                RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_APPLICATION, applicationId, log);
                return null;
            }

            // If application creation workflow status is pending or rejected, throw a Bad request exception
            if (application.getStatus().equals(WorkflowStatus.REJECTED.toString())
                    || application.getStatus().equals(WorkflowStatus.CREATED.toString())) {
                RestApiUtil.handleBadRequest("Workflow status is not Approved", log);
                return null;
            }

            if (APIConstants.DEFAULT_SUB_POLICY_SUBSCRIPTIONLESS.equalsIgnoreCase(body.getThrottlingPolicy())
                    || APIConstants.DEFAULT_SUB_POLICY_ASYNC_SUBSCRIPTIONLESS
                    .equalsIgnoreCase(body.getThrottlingPolicy())) {
                throw new APIManagementException("Subscribing to the API with an internal business plan is not allowed",
                        ExceptionCodes.from(ExceptionCodes.BUSINESS_PLAN_NOT_ALLOWED, body.getThrottlingPolicy()));
            }

            if (!RestAPIStoreUtils.isUserAccessAllowedForApplication(application)) {
                //application access failure occurred
                RestApiUtil.handleAuthorizationFailure(RestApiConstants.RESOURCE_APPLICATION, applicationId, log);
            }

            ApiTypeWrapper apiTypeWrapper = apiConsumer.getAPIorAPIProductByUUID(body.getApiId(), organization);

            OrganizationInfo userOrgInfo = RestApiUtil.getOrganizationInfo(messageContext);
            userOrgInfo.setSuperOrganization(organization);
            if (!apiTypeWrapper.isAPIProduct() && !StringUtils.isEmpty(userOrgInfo.getOrganizationId())) {
                org.wso2.carbon.apimgt.rest.api.store.v1.utils.APIUtils
                        .updateAvailableTiersByOrganization(apiTypeWrapper.getApi(), userOrgInfo.getOrganizationId());
            }

            apiTypeWrapper.setTier(body.getThrottlingPolicy());

            SubscriptionResponse subscriptionResponse = apiConsumer
                    .addSubscription(apiTypeWrapper, username, application);
            SubscribedAPI addedSubscribedAPI = apiConsumer
                    .getSubscriptionByUUID(subscriptionResponse.getSubscriptionUUID());
            SubscriptionDTO addedSubscriptionDTO = SubscriptionMappingUtil.fromSubscriptionToDTO(addedSubscribedAPI,
                    apiTypeWrapper, organization);
            WorkflowResponse workflowResponse = subscriptionResponse.getWorkflowResponse();
            if (workflowResponse instanceof HttpWorkflowResponse) {
                String payload = workflowResponse.getJSONPayload();
                addedSubscriptionDTO.setRedirectionParams(payload);
            }

            return Response.created(new URI(RestApiConstants.RESOURCE_PATH_SUBSCRIPTIONS + "/" +
                    addedSubscribedAPI.getUUID())).entity(addedSubscriptionDTO).build();

        } catch (APIMgtAuthorizationFailedException e) {
            //this occurs when the api:application:tier mapping is not allowed. The reason for the message is taken from
            // the message of the exception e
            RestApiUtil.handleAuthorizationFailure(e.getMessage(), e, log);
        } catch (SubscriptionAlreadyExistingException e) {
            RestApiUtil.handleResourceAlreadyExistsError(
                    "Specified subscription already exists for API " + body.getApiId() + ", for application "
                            + body.getApplicationId(), e, log);
        } catch (SubscriptionBlockedException e) {
            RestApiUtil.handleOperationBlockedError("Subscription blocked. " + e.getMessage()
                    + ". Please contact the API publisher.", e, log);
        } catch (URISyntaxException e) {
            if (RestApiUtil.isDueToResourceNotFound(e)) {
                //this happens when the specified API identifier does not exist
                RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_API, body.getApiId(), e, log);
            } else {
                //unhandled exception
                RestApiUtil.handleInternalServerError(
                        "Error while adding the subscription API:" + body.getApiId() + ", application:" + body
                                .getApplicationId() + ", tier:" + body.getThrottlingPolicy(), e, log);
            }
        }
        return null;
    }

    /**
     * Update already created subscriptions with the details specified in the body parameter
     *
     * @param body new subscription details
     * @return newly added subscription as a SubscriptionDTO if successful
     */
    @Override
    public Response subscriptionsSubscriptionIdPut(String subscriptionId, SubscriptionDTO body, String xWSO2Tenant,
                                                   MessageContext messageContext) {
        String username = RestApiCommonUtil.getLoggedInUsername();
        APIConsumer apiConsumer;

        try {
            String organization = RestApiUtil.getValidatedOrganization(messageContext);
            apiConsumer = RestApiCommonUtil.getConsumer(username);
            String applicationId = body.getApplicationId();
            String currentThrottlingPolicy = body.getThrottlingPolicy();
            String requestedThrottlingPolicy = body.getRequestedThrottlingPolicy();

            SubscribedAPI subscribedAPI = apiConsumer.getSubscriptionByUUID(subscriptionId);
            //Check whether the subscription status is not empty and also not blocked
            if (body.getStatus() != null && subscribedAPI != null) {
                if ("BLOCKED".equals(body.getStatus().value()) || "ON_HOLD".equals(body.getStatus().value())
                        || "PROD_ONLY_BLOCKED".equals(body.getStatus().value()) || "PROD_ONLY_BLOCKED".equals(subscribedAPI.getSubStatus())
                        || "REJECTED".equals(body.getStatus().value()) || "BLOCKED".equals(subscribedAPI.getSubStatus())
                        || "ON_HOLD".equals(subscribedAPI.getSubStatus())
                        || "REJECTED".equals(subscribedAPI.getSubStatus())) {
                    RestApiUtil.handleBadRequest(
                            "Cannot update subscriptions with provided or existing status", log);
                    return null;
                }
            } else {
                RestApiUtil.handleBadRequest(
                        "Request must contain status of the subscription", log);
                return null;
            }

            //check whether user is permitted to access the API. If the API does not exist,
            // this will throw a APIMgtResourceNotFoundException
            if (body.getApiId() != null) {
                if (!RestAPIStoreUtils.isUserAccessAllowedForAPIByUUID(body.getApiId(), organization)) {
                    RestApiUtil.handleAuthorizationFailure(RestApiConstants.RESOURCE_API, body.getApiId(), log);
                }
            } else {
                RestApiUtil.handleBadRequest(
                        "Request must contain either apiIdentifier or apiProductIdentifier and the relevant type", log);
                return null;
            }

            Application application = apiConsumer.getApplicationByUUID(applicationId);
            if (application == null) {
                //required application not found
                RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_APPLICATION, applicationId, log);
                return null;
            }

            if (!RestAPIStoreUtils.isUserAccessAllowedForApplication(application)) {
                //application access failure occurred
                RestApiUtil.handleAuthorizationFailure(RestApiConstants.RESOURCE_APPLICATION, applicationId, log);
            }

            if (APIConstants.DEFAULT_SUB_POLICY_SUBSCRIPTIONLESS.equalsIgnoreCase(body.getRequestedThrottlingPolicy())
                    || APIConstants.DEFAULT_SUB_POLICY_ASYNC_SUBSCRIPTIONLESS
                    .equalsIgnoreCase(body.getRequestedThrottlingPolicy())) {
                throw new APIManagementException(ExceptionCodes.from(ExceptionCodes.BUSINESS_PLAN_NOT_ALLOWED,
                        body.getRequestedThrottlingPolicy()));
            }

            ApiTypeWrapper apiTypeWrapper = apiConsumer.getAPIorAPIProductByUUID(body.getApiId(), organization);

            OrganizationInfo userOrgInfo = RestApiUtil.getOrganizationInfo(messageContext);
            userOrgInfo.setSuperOrganization(organization);
            if (!apiTypeWrapper.isAPIProduct() && !StringUtils.isEmpty(userOrgInfo.getOrganizationId())) {
                org.wso2.carbon.apimgt.rest.api.store.v1.utils.APIUtils
                        .updateAvailableTiersByOrganization(apiTypeWrapper.getApi(), userOrgInfo.getOrganizationId());
            }

            apiTypeWrapper.setTier(body.getThrottlingPolicy());

            SubscriptionResponse subscriptionResponse = apiConsumer
                    .updateSubscription(apiTypeWrapper, username, application, subscriptionId,
                            currentThrottlingPolicy, requestedThrottlingPolicy);
            SubscribedAPI addedSubscribedAPI = apiConsumer
                    .getSubscriptionByUUID(subscriptionResponse.getSubscriptionUUID());
            SubscriptionDTO addedSubscriptionDTO = SubscriptionMappingUtil.fromSubscriptionToDTO(addedSubscribedAPI,
                    organization);
            WorkflowResponse workflowResponse = subscriptionResponse.getWorkflowResponse();
            if (workflowResponse instanceof HttpWorkflowResponse) {
                String payload = workflowResponse.getJSONPayload();
                addedSubscriptionDTO.setRedirectionParams(payload);
            }

            return Response.ok(new URI(RestApiConstants.RESOURCE_PATH_SUBSCRIPTIONS + "/" +
                    addedSubscribedAPI.getUUID())).entity(addedSubscriptionDTO).build();

        } catch (APIMgtAuthorizationFailedException e) {
            //this occurs when the api:application:tier mapping is not allowed. The reason for the message is taken from
            // the message of the exception e
            RestApiUtil.handleAuthorizationFailure(e.getMessage(), e, log);
        } catch (SubscriptionAlreadyExistingException e) {
            RestApiUtil.handleResourceAlreadyExistsError(
                    "Specified subscription already exists for API " + body.getApiId() + ", for application "
                            + body.getApplicationId(), e, log);
        } catch (SubscriptionBlockedException e) {
            RestApiUtil.handleOperationBlockedError("Subscription blocked. " + e.getMessage()
                    + ". Please contact the API publisher.", e, log);
        } catch (APIManagementException | URISyntaxException e) {
            if (RestApiUtil.isDueToResourceNotFound(e)) {
                //this happens when the specified API identifier does not exist
                RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_API, body.getApiId(), e, log);
            } else {
                //unhandled exception
                RestApiUtil.handleInternalServerError(
                        "Error while adding the subscription API:" + body.getApiId() + ", application:" + body
                                .getApplicationId() + ", tier:" + body.getThrottlingPolicy(), e, log);
            }
        }
        return null;
    }

    /**
     * Create multiple new subscriptions with the list of subscription details specified in the body parameter.
     *
     * @param body list of new subscription details
     * @return list of newly added subscription as a SubscriptionDTO if successful
     */
    @Override
    public Response subscriptionsMultiplePost(List<SubscriptionDTO> body, String xWSO2Tenant,
                                              MessageContext messageContext) throws APIManagementException {
        String username = RestApiCommonUtil.getLoggedInUsername();
        String organization = RestApiUtil.getValidatedOrganization(messageContext);
        List<SubscriptionDTO> subscriptions = new ArrayList<>();
        for (SubscriptionDTO subscriptionDTO : body) {
            try {
                APIConsumer apiConsumer = RestApiCommonUtil.getConsumer(username);
                String applicationId = subscriptionDTO.getApplicationId();
                APIIdentifier apiIdentifier = APIMappingUtil
                        .getAPIIdentifierFromUUID(subscriptionDTO.getApiId(), organization);

                //check whether user is permitted to access the API. If the API does not exist,
                // this will throw a APIMgtResourceNotFoundException
                if (!org.wso2.carbon.apimgt.rest.api.util.utils.RestAPIStoreUtils
                        .isUserAccessAllowedForAPIByUUID(subscriptionDTO.getApiId(), organization)) {
                    RestApiUtil.handleAuthorizationFailure(RestApiConstants.RESOURCE_API,
                            subscriptionDTO.getApiId(), log);
                }

                Application application = apiConsumer.getApplicationByUUID(applicationId);
                if (application == null) {
                    //required application not found
                    RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_APPLICATION, applicationId, log);
                }

                if (!RestAPIStoreUtils.isUserAccessAllowedForApplication(application)) {
                    //application access failure occurred
                    RestApiUtil.handleAuthorizationFailure(RestApiConstants.RESOURCE_APPLICATION, applicationId, log);
                }

                ApiTypeWrapper apiTypeWrapper = apiConsumer.getAPIorAPIProductByUUID(subscriptionDTO.getApiId(),
                        organization);

                OrganizationInfo userOrgInfo = RestApiUtil.getOrganizationInfo(messageContext);
                userOrgInfo.setSuperOrganization(organization);
                if (!apiTypeWrapper.isAPIProduct() && !StringUtils.isEmpty(userOrgInfo.getOrganizationId())) {
                    org.wso2.carbon.apimgt.rest.api.store.v1.utils.APIUtils.updateAvailableTiersByOrganization(
                            apiTypeWrapper.getApi(), userOrgInfo.getOrganizationId());
                }
                apiTypeWrapper.setTier(subscriptionDTO.getThrottlingPolicy());
                SubscriptionResponse subscriptionResponse = apiConsumer
                        .addSubscription(apiTypeWrapper, username, application);
                SubscribedAPI addedSubscribedAPI = apiConsumer
                        .getSubscriptionByUUID(subscriptionResponse.getSubscriptionUUID());
                SubscriptionDTO addedSubscriptionDTO =
                        SubscriptionMappingUtil.fromSubscriptionToDTO(addedSubscribedAPI, organization);
                subscriptions.add(addedSubscriptionDTO);

            } catch (APIMgtAuthorizationFailedException e) {
                //this occurs when the api:application:tier mapping is not allowed. The reason for the message is
                // taken from the message of the exception e
                RestApiUtil.handleAuthorizationFailure(e.getMessage(), e, log);
            } catch (SubscriptionAlreadyExistingException e) {
                RestApiUtil.handleResourceAlreadyExistsError(
                        "Specified subscription already exists for API " + subscriptionDTO.getApiId() +
                                " for application " + subscriptionDTO.getApplicationId(), e, log);
            } catch (APIManagementException e) {
                if (RestApiUtil.isDueToResourceNotFound(e)) {
                    //this happens when the specified API identifier does not exist
                    RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_API, subscriptionDTO.getApiId(),
                            e, log);
                } else {
                    //unhandled exception
                    RestApiUtil.handleInternalServerError(
                            "Error while adding the subscription API:" + subscriptionDTO.getApiId() +
                                    ", application:" + subscriptionDTO.getApplicationId() + ", throttling policy:" +
                                    subscriptionDTO.getThrottlingPolicy(), e, log);
                }
            }
        }
        return Response.ok().entity(subscriptions).build();
    }

    /**
     * Gets a subscription by identifier
     *
     * @param subscriptionId subscription identifier
     * @param ifNoneMatch    If-None-Match header value
     * @return matched subscription as a SubscriptionDTO
     */
    @Override
    public Response subscriptionsSubscriptionIdGet(String subscriptionId, String ifNoneMatch,
                                                   MessageContext messageContext) {
        String username = RestApiCommonUtil.getLoggedInUsername();
        APIConsumer apiConsumer;
        try {
            String organization = RestApiUtil.getValidatedOrganization(messageContext);
            apiConsumer = RestApiCommonUtil.getConsumer(username);
            SubscribedAPI subscribedAPI = validateAndGetSubscription(subscriptionId, apiConsumer);
            SubscriptionDTO subscriptionDTO = SubscriptionMappingUtil.fromSubscriptionToDTO(subscribedAPI,
                    organization);
            return Response.ok().entity(subscriptionDTO).build();
        } catch (APIManagementException e) {
            RestApiUtil.handleInternalServerError("Error while getting subscription with id " + subscriptionId, e, log);
        }
        return null;
    }

    @Override
    public Response subscriptionsSubscriptionIdUsageGet(String subscriptionId, MessageContext messageContext) {

        if (StringUtils.isBlank(subscriptionId)) {
            String errorMessage = "Subscription ID cannot be empty or null when getting monetization usage.";
            RestApiUtil.handleBadRequest(errorMessage, log);
        }
        try {
            APIConsumer apiConsumer = RestApiCommonUtil.getLoggedInUserConsumer();
            Monetization monetizationImplementation = apiConsumer.getMonetizationImplClass();
            Map<String, String> billingEngineUsageData = monetizationImplementation.
                    getCurrentUsageForSubscription(subscriptionId, RestApiCommonUtil.getLoggedInUserProvider());
            if (MapUtils.isEmpty(billingEngineUsageData)) {
                String errorMessage = "Billing engine usage data was not found for subscription ID : " + subscriptionId;
                RestApiUtil.handleBadRequest(errorMessage, log);
            }
            APIMonetizationUsageDTO apiMonetizationUsageDTO = new APIMonetizationUsageDTO();
            apiMonetizationUsageDTO.setProperties(billingEngineUsageData);
            return Response.ok().entity(apiMonetizationUsageDTO).build();
        } catch (APIManagementException e) {
            String errorMessage = "Failed to retrieve billing engine usage data for subscription ID : " + subscriptionId;
            RestApiUtil.handleInternalServerError(errorMessage, e, log);
        } catch (MonetizationException e) {
            String errorMessage = "Failed to get current usage for subscription ID : " + subscriptionId;
            RestApiUtil.handleInternalServerError(errorMessage, e, log);
        }
        return null;
    }

    /**
     * Deletes the subscription matched to subscription id
     *
     * @param subscriptionId subscription identifier
     * @param ifMatch        If-Match header value
     * @return 200 response if successfully deleted the subscription
     */
    @Override
    public Response subscriptionsSubscriptionIdDelete(String subscriptionId, String ifMatch,
                                                      MessageContext messageContext) {
        String username = RestApiCommonUtil.getLoggedInUsername();
        APIConsumer apiConsumer;
        try {
            apiConsumer = RestApiCommonUtil.getConsumer(username);
            SubscribedAPI subscribedAPI = validateAndGetSubscription(subscriptionId, apiConsumer);
            String organization = RestApiUtil.getValidatedOrganization(messageContext);
            apiConsumer.removeSubscription(subscribedAPI, organization);
            if (subscribedAPI != null &&
                    APIConstants.SubscriptionStatus.DELETE_PENDING.equals(subscribedAPI.getSubStatus())) {
                if (subscribedAPI.getSubscriptionId() == -1) {
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                return Response.status(Response.Status.CREATED).build();
            }
            return Response.ok().build();
        } catch (APIManagementException e) {
            RestApiUtil
                    .handleInternalServerError("Error while deleting subscription with id " + subscriptionId, e, log);
        }
        return null;
    }

    private SubscribedAPI validateAndGetSubscription(String subscriptionId, APIConsumer apiConsumer)
            throws APIManagementException {
        SubscribedAPI subscribedAPI = apiConsumer.getSubscriptionByUUID(subscriptionId);
        if (subscribedAPI == null) {
            RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_SUBSCRIPTION, subscriptionId, log);
            return null;
        }
        if (!RestAPIStoreUtils.isUserAccessAllowedForApplication(subscribedAPI.getApplication())) {
            RestApiUtil.handleAuthorizationFailure(RestApiConstants.RESOURCE_SUBSCRIPTION, subscriptionId, log);
        }
        return subscribedAPI;
    }

    /**
     * Get additional Info details of subscriptions attached with given API
     *
     * @param apiId         apiId
     * @param offset        starting index of the subscription list
     * @param limit         max num of subscriptions returned
     * @param ifNoneMatch   If-None-Match header value
     * @param messageContext message context
     * @return Response with additional Info of the GraphQL API
     */
    @Override
    public Response getAdditionalInfoOfAPISubscriptions(String apiId, String groupId, String xWSO2Tenant, Integer offset,
                    Integer limit, String ifNoneMatch, MessageContext messageContext) {

        String username = RestApiCommonUtil.getLoggedInUsername();
        Subscriber subscriber = new Subscriber(username);
        Set<SubscribedAPI> subscriptions;
        List<SubscribedAPI> subscribedAPIList = new ArrayList<>();

        try {
            String organization = RestApiUtil.getValidatedOrganization(messageContext);
            APIConsumer apiConsumer = RestApiCommonUtil.getConsumer(username);
            AdditionalSubscriptionInfoListDTO additionalSubscriptionInfoListDTO = null;

            ApiTypeWrapper apiTypeWrapper = apiConsumer.getAPIorAPIProductByUUID(apiId, organization);

            if (apiTypeWrapper.isAPIProduct()) {
                subscriptions = apiConsumer.getSubscribedIdentifiers(subscriber,
                        apiTypeWrapper.getApiProduct().getId(), groupId, organization);
            } else {
                subscriptions = apiConsumer.getSubscribedIdentifiers(subscriber,
                        apiTypeWrapper.getApi().getId(), groupId, organization);
            }

            //Sort subscriptions by application name
            subscribedAPIList.addAll(subscriptions);
            subscribedAPIList.sort(Comparator.comparing(o -> o.getApplication().getName()));
            additionalSubscriptionInfoListDTO = AdditionalSubscriptionInfoMappingUtil
                    .fromAdditionalSubscriptionInfoListToDTO(subscribedAPIList, limit, offset, organization);
            AdditionalSubscriptionInfoMappingUtil.setPaginationParams(additionalSubscriptionInfoListDTO, apiId,
                    "", limit, offset, subscribedAPIList.size());
            return Response.ok().entity(additionalSubscriptionInfoListDTO).build();

        } catch (APIManagementException e) {
            //Auth failure occurs when cross tenant accessing APIs. Sends 404, since we don't need
            // to expose the existence of the resource
            if (RestApiUtil.isDueToResourceNotFound(e)) {
                RestApiUtil.handleResourceNotFoundError(RestApiConstants.RESOURCE_API, apiId, e, log);
            } else if (RestApiUtil.isDueToAuthorizationFailure(e)) {
                RestApiUtil.handleAuthorizationFailure(
                        "Authorization failure while retrieving additional information details of the API : " + apiId, e, log);
            } else {
                String msg = "Error while retrieving additional information details of the API " + apiId;
                RestApiUtil.handleInternalServerError(msg, e, log);
            }
        }
        return null;
    }
}
