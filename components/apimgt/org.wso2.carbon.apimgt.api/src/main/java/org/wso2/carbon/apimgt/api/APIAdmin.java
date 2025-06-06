/*
*  Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.carbon.apimgt.api;

import org.wso2.carbon.apimgt.api.dto.GatewayVisibilityPermissionConfigurationDTO;
import org.wso2.carbon.apimgt.api.dto.KeyManagerConfigurationDTO;
import org.wso2.carbon.apimgt.api.dto.KeyManagerPermissionConfigurationDTO;
import org.wso2.carbon.apimgt.api.dto.OrganizationDetailsDTO;
import org.wso2.carbon.apimgt.api.model.APICategory;
import org.wso2.carbon.apimgt.api.model.ApiResult;
import org.wso2.carbon.apimgt.api.model.Application;
import org.wso2.carbon.apimgt.api.model.ApplicationInfo;
import org.wso2.carbon.apimgt.api.model.Environment;
import org.wso2.carbon.apimgt.api.model.LLMProvider;
import org.wso2.carbon.apimgt.api.model.Label;
import org.wso2.carbon.apimgt.api.model.Monetization;
import org.wso2.carbon.apimgt.api.model.MonetizationUsagePublishInfo;
import org.wso2.carbon.apimgt.api.model.Workflow;
import org.wso2.carbon.apimgt.api.model.botDataAPI.BotDetectionData;
import org.wso2.carbon.apimgt.api.model.policy.Policy;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * APIAdmin responsible for providing helper functionality
 */
public interface APIAdmin  {
    /**
     * Returns environments of a given tenant
     *
     * @param tenantDomain tenant domain
     * @return List of environments related to the given tenant
     */
    List<Environment> getAllEnvironments(String tenantDomain) throws APIManagementException;

    /**
     * Returns environment of a given uuid
     *
     * @param tenantDomain tenant domain
     * @return List of environments related to the given tenant
     */
    Environment getEnvironment(String tenantDomain, String uuid) throws APIManagementException;

    /**
     * Creates a new environment in the tenant
     *
     * @param tenantDomain tenant domain
     * @param environment  content to add
     * @return Added environment
     * @throws APIManagementException if failed add environment
     */
    Environment addEnvironment(String tenantDomain, Environment environment) throws APIManagementException;

    /**
     * Delete an existing environment
     *
     * @param tenantDomain tenant domain
     * @param uuid         Environment identifier
     * @throws APIManagementException If failed to delete environment
     */
    void deleteEnvironment(String tenantDomain, String uuid) throws APIManagementException;

    /**
     * Checks whether the given environment has active gateway policy deployments.
     *
     * @param tenantDomain tenant domain
     * @param uuid         Environment identifier
     * @return true if environment exists
     * @throws APIManagementException If failed to check environment existence
     */
    boolean hasExistingDeployments(String tenantDomain, String uuid) throws APIManagementException;

    /**
     * Updates the details of the given Environment.
     *
     * @param tenantDomain tenant domain
     * @param environment  content to update
     * @return updated environment
     * @throws APIManagementException if failed to update environment
     */
    Environment updateEnvironment(String tenantDomain, Environment environment) throws APIManagementException;



    Application[] getAllApplicationsOfTenantForMigration(String appTenantDomain) throws APIManagementException;

    /**
     * Returns List of Applications
     * @param user              Logged-in user
     * @param owner             Owner of the application
     * @param tenantId          Logged-in user tenant-id
     * @param limit             The limit
     * @param offset            The offset
     * @param applicationName   The application name
     * @param sortBy            The sortBy column
     * @param sortOrder         The sort order
     * @return          List of applications match to the search conditions
     * @throws APIManagementException
     */
    Application[] getApplicationsWithPagination(String user, String owner, int tenantId, int limit , int offset,
                                                String applicationName, String sortBy, String sortOrder)
            throws APIManagementException;

    /**
     * Get count of the applications for the tenantId.
     *
     * @param tenantId             content to get application count based on tenant_id
     * @param searchOwner          content to search applications based on owners
     * @param searchApplication    content to search applications based on application
     * @throws APIManagementException if failed to get application
     */

    public int getApplicationsCount(int tenantId, String searchOwner, String searchApplication)
            throws APIManagementException;

    /**
     * This methods loads the monetization implementation class
     *
     * @return monetization implementation class
     * @throws APIManagementException if failed to load monetization implementation class
     */
    Monetization getMonetizationImplClass() throws APIManagementException;

    /**
     * Get the info about the monetization usage publish job
     *
     * @throws APIManagementException if failed to get monetization usage publish info
     */
    MonetizationUsagePublishInfo getMonetizationUsagePublishInfo() throws APIManagementException;

    /**
     * Add the info about the monetization usage publish job
     *
     * @throws APIManagementException if failed to update monetization usage publish info
     */
    void addMonetizationUsagePublishInfo(MonetizationUsagePublishInfo monetizationUsagePublishInfo)
            throws APIManagementException;

    /**
     * Update the info about the monetization usage publish job
     *
     * @throws APIManagementException if failed to update monetization usage publish info
     */
    void updateMonetizationUsagePublishInfo(MonetizationUsagePublishInfo monetizationUsagePublishInfo)
            throws APIManagementException;

    /**
     * Add a bot detection alert subscription
     *
     * @param email email to be registered for the subscription
     * @throws APIManagementException if an error occurs when adding a bot detection alert subscription
     */
    void addBotDetectionAlertSubscription(String email) throws APIManagementException;

    /**
     * Delete a bot detection alert subscription
     *
     * @param uuid uuid of the subscription
     * @throws APIManagementException if an error occurs when deleting a bot detection alert subscription
     */
    void deleteBotDetectionAlertSubscription(String uuid) throws APIManagementException;

    /**
     * Retrieve a bot detection alert subscription by querying a particular field (uuid or email)
     *
     * @param field field to be queried to obtain the bot detection alert subscription. Can be uuid or email
     * @param value value corresponding to the field (uuid or email value)
     * @return if a subscription exists, returns the bot detection alert subscription, else returns a null object
     * @throws APIManagementException if an error occurs when retrieving a bot detection alert subscription
     */
    BotDetectionData getBotDetectionAlertSubscription(String field, String value) throws APIManagementException;

    /**
     * Retrieve all bot detection alert subscriptions
     *
     * @throws APIManagementException if an error occurs when retrieving bot detection alert subscriptions
     */
    List<BotDetectionData> getBotDetectionAlertSubscriptions() throws APIManagementException;

    /**
     * Retrieve all bot detected data
     *
     * @return list of bot detected data
     * @throws APIManagementException if an error occurs when retrieving bot detection data
     */
    List<BotDetectionData> retrieveBotDetectionData() throws APIManagementException;

    /**
     * Adds a new category for the tenant
     *
     * @param userName      logged in user name
     * @param category      category to add
     * @param organization  organization
     * @throws APIManagementException if failed add category
     */
    APICategory addCategory(APICategory category, String userName, String organization) throws APIManagementException;

    /**
     * Updates an API Category
     *
     * @param   apiCategory
     * @throws APIManagementException
     */
    void updateCategory(APICategory apiCategory) throws APIManagementException;

    /**
     * Delete an API Category
     *
     * @param categoryID
     * @param username
     * @throws APIManagementException
     */
    void deleteCategory(String categoryID, String username) throws APIManagementException;

    /**
     * Checks whether an api category exists by the given name
     *
     * 1. in case uuid is null : checks whether the categoryName is already taken in the tenantDomain (this
     *                           flow is used when adding a new api category)
     * 2. in case uuid is not null: checks whether the categoryName is already taken by any category other than the one
     *                              defined by the passed uuid in the given tenant
     *
     * @param categoryName
     * @param organization
     * @return true if an api category exists by the given category name
     * @throws APIManagementException
     */
    boolean isCategoryNameExists(String categoryName, String uuid, String organization) throws APIManagementException;

    /**
     * Returns all api categories of the organization
     * @param organization  Organization
     * @return
     * @throws APIManagementException
     */
    List<APICategory> getAllAPICategoriesOfOrganization(String organization) throws APIManagementException;

    /**
     * Returns all api categories of the organization with number of APIs for each category
     *
     * @param organization organization of the API
     * @return
     * @throws APIManagementException
     */
    List<APICategory> getAPICategoriesOfOrganization(String organization) throws APIManagementException;

    /**
     * Get API Category identified by the given uuid
     *
     * @param apiCategoryId api category UUID
     * @return
     * @throws APIManagementException
     */
    APICategory getAPICategoryByID(String apiCategoryId) throws APIManagementException;

    /**
     * Adds a new label for the tenant
     *
     * @param label      label to add
     * @param tenantDomain tenant domain
     * @throws APIManagementException if failed add label
     */
    Label addLabel(Label label, String tenantDomain) throws APIManagementException;

    /**
     * Updates a label
     *
     * @param labelID       label ID to update
     * @param updateLabelBody   label data to update
     * @param tenantDomain tenant domain
     * @throws APIManagementException if failed update label
     */
    Label updateLabel(String labelID, Label updateLabelBody, String tenantDomain) throws APIManagementException;

    /**
     * Delete a label
     *
     * @param labelID       label ID to delete
     * @param tenantDomain tenant domain
     * @throws APIManagementException if failed delete label
     */
    void deleteLabel(String labelID, String tenantDomain) throws APIManagementException;

    /**
     * Returns all labels of the tenant
     *
     * @param tenantDomain  tenant domain
     * @return List<Label> list of Label objects
     * @throws APIManagementException if failed to get labels
     */
    List<Label> getAllLabelsOfTenant(String tenantDomain) throws APIManagementException;

    /**
     * Get mapped APIs for the given label
     *
     * @param labelID label UUID
     * @param tenantDomain  tenant domain
     * @return List<ApiResult> list of ApiResult objects
     * @throws APIManagementException
     */
    List<ApiResult> getMappedApisForLabel(String labelID, String tenantDomain) throws APIManagementException;

    /**
     * The method converts the date into timestamp
     *
     * @param date
     * @return Timestamp in long format
     */
    long getTimestamp(String date);

    /**
     * This method used to retrieve key manager configurations for tenant
     * @param organization organization of the key manager
     * @param checkUsages whether to check usages
     * @return KeyManagerConfigurationDTO list
     * @throws APIManagementException if error occurred
     */
    List<KeyManagerConfigurationDTO> getKeyManagerConfigurationsByOrganization(String organization,
            boolean checkUsages) throws APIManagementException;

    /**
     * This method returns all the key managers registered in all the tenants
     * @return
     * @throws APIManagementException
     */
    Map<String, List<KeyManagerConfigurationDTO>> getAllKeyManagerConfigurations() throws APIManagementException;

    /**
     * This method used to retrieve key manager with Id in respective tenant
     * @param organization organization requested
     * @param id uuid of key manager
     * @return KeyManagerConfigurationDTO for retrieved data
     * @throws APIManagementException
     */
    KeyManagerConfigurationDTO getKeyManagerConfigurationById(String organization, String id)
            throws APIManagementException;

    /**
     * This method is used to check IDP is in the given organization
     *
     * @param organization organization uuid
     * @param resourceId   IDP resource ID
     * @return boolean indication of it's existence
     * @throws APIManagementException
     */
    boolean isIDPExistInOrg(String organization, String resourceId) throws APIManagementException;

    /**
     * Used to get organization UUID of a application by giving consumer key.
     *
     * @param consumerKey consumer key of the application
     * @return ApplicationInfo details of a application
     * @throws APIManagementException
     */
    ApplicationInfo getLightweightApplicationByConsumerKey(String consumerKey) throws APIManagementException;

    /**
     * This method used to check existence of key manager with Id in respective tenant
     * @param organization organization requested
     * @param id uuid of key manager
     * @return existence
     * @throws APIManagementException
     */
    boolean isKeyManagerConfigurationExistById(String organization, String id) throws APIManagementException;

    /**
     * This method used to create key Manager
     * @param keyManagerConfigurationDTO key manager data
     * @return created key manager
     * @throws APIManagementException
     */
    KeyManagerConfigurationDTO addKeyManagerConfiguration(KeyManagerConfigurationDTO keyManagerConfigurationDTO)
            throws APIManagementException;
    /**
     * This method used to update key Manager
     * @param keyManagerConfigurationDTO key manager data
     * @return updated key manager
     * @throws APIManagementException
     */
    KeyManagerConfigurationDTO updateKeyManagerConfiguration(KeyManagerConfigurationDTO keyManagerConfigurationDTO)
            throws APIManagementException;

    /**
     * This method used to get key manager permissions with key manager id and role
     * @param id uuid of key manager
     * @return key manager permissions
     * @throws APIManagementException
     */
    KeyManagerPermissionConfigurationDTO getKeyManagerPermissions(String id) throws APIManagementException;

    /**
     * This method used to get gateway visibility permissions with gateway environment id and role
     * @param id uuid of gateway environment
     * @return gateway visibility permissions
     * @throws APIManagementException
     */
    GatewayVisibilityPermissionConfigurationDTO getGatewayVisibilityPermissions(String id) throws APIManagementException;

    /**
     * hTis method used to delete IDP mapped with key manager
     * @param organization organization requested
     * @param keyManagerConfigurationDTO key manager data
     * @throws APIManagementException
     */
    void deleteIdentityProvider(String organization, KeyManagerConfigurationDTO keyManagerConfigurationDTO)
            throws APIManagementException;

    /**
     * This method used to delete key manager
     * @param organization organization requested
     * @param keyManagerConfigurationDTO  key manager data
     * @throws APIManagementException
     */
    void deleteKeyManagerConfigurationById(String organization, KeyManagerConfigurationDTO keyManagerConfigurationDTO)
            throws APIManagementException;

    /**
     * This method used to retrieve key manager from name
     * @param organization organization requested
     * @param name name requested
     * @return keyManager data
     * @throws APIManagementException
     */
    KeyManagerConfigurationDTO getKeyManagerConfigurationByName(String organization, String name)
            throws APIManagementException;

    /**
     * The method get all the pending workflow requests
     *
     * @param workflowType
     * @param status
     * @param tenantDomain
     * @return Workflow[]
     * @throws APIManagementException
     */
    Workflow[] getworkflows(String workflowType, String status, String tenantDomain) throws APIManagementException;

    /**
     * The method get all the pending workflow requests
     *
     * @param externelWorkflowRef
     * @param status
     * @param tenantDomain
     * @return Workflow
     * @throws APIManagementException
     */
    Workflow getworkflowReferenceByExternalWorkflowReferenceID(String externelWorkflowRef, String status, String tenantDomain)
            throws APIManagementException;

    /**
     * This method used to check the existence of the scope name for the particular user
     * @param username user to be validated
     * @param scopeName scope name to be checked
     * @return true if a scope exists by the given username
     * @throws APIManagementException
     */
    boolean isScopeExistsForUser(String username, String scopeName)
            throws APIManagementException;

    /**
     * This method used to check the existence of the scope name
     * @param username logged in username to get the tenantDomain
     * @param scopeName scope name to be checked
     * @return true if a scope exists
     * @throws APIManagementException
     */
    boolean isScopeExists(String username, String scopeName)
            throws APIManagementException;

    /**
     * Adds a tenant theme to the database
     *
     * @param tenantId     tenant ID of user
     * @param themeContent content of the tenant theme
     * @throws APIManagementException if an error occurs when adding a tenant theme to the database
     */
    void addTenantTheme(int tenantId, InputStream themeContent) throws APIManagementException;

    /**
     * Updates an existing tenant theme in the database
     *
     * @param tenantId     tenant ID of user
     * @param themeContent content of the tenant theme
     * @throws APIManagementException if an error occurs when updating an existing tenant theme in the database
     */
    void updateTenantTheme(int tenantId, InputStream themeContent) throws APIManagementException;

    /**
     * Retrieves a tenant theme from the database
     *
     * @param tenantId tenant ID of user
     * @return content of the tenant theme
     * @throws APIManagementException if an error occurs when retrieving a tenant theme from the database
     */
    InputStream getTenantTheme(int tenantId) throws APIManagementException;

    /**
     * Checks whether a tenant theme exist for a particular tenant
     *
     * @param tenantId tenant ID of user
     * @return true if a tenant theme exist for a particular tenant ID, false otherwise
     * @throws APIManagementException if an error occurs when determining whether a tenant theme exists for a given
     *                                tenant ID
     */
    boolean isTenantThemeExist(int tenantId) throws APIManagementException;

    /**
     * Deletes a tenant theme from the database
     *
     * @param tenantId tenant ID of user
     * @throws APIManagementException if an error occurs when deleting a tenant theme from the database
     */
    void deleteTenantTheme(int tenantId) throws APIManagementException;

    String getTenantConfig(String organization) throws APIManagementException;

    /**
     * Imports a drafted organization theme for the given organization.
     *
     * @param organization Organization name.
     * @param themeContent Theme content as InputStream.
     * @throws APIManagementException If a database error occurs.
     */
    void importDraftedOrgTheme(String organization, InputStream themeContent) throws APIManagementException;

    /**
     * Updates the organization theme status as published or unpublished.
     *
     * @param organization Organization name.
     * @param action       Action to perform ("PUBLISH" or "UNPUBLISH").
     * @throws APIManagementException If a database error occurs.
     */
    void updateOrgThemeStatus(String organization, String action) throws APIManagementException;

    /**
     * Deletes an organization theme.
     *
     * @param organization Organization name.
     * @param themeId      Theme ID to delete.
     * @throws APIManagementException If a database error occurs.
     */
    void deleteOrgTheme(String organization, String themeId) throws APIManagementException;

    /**
     * Gets an organization theme.
     *
     * @param uuid         Theme ID to retrieve.
     * @param organization Organization name.
     * @return Input stream of Org theme.
     * @throws APIManagementException If a database error occurs.
     */
    InputStream getOrgTheme(String uuid, String organization) throws APIManagementException;

    /**
     * Retrieves the themes associated with the given organization.
     *
     * @param organization Organization name.
     * @return Hash map of publish unpublish state and theme IDs.
     * @throws APIManagementException If a database error occurs.
     */
    Map<String, String> getOrgThemes(String organization) throws APIManagementException;

    void updateTenantConfig(String organization, String config) throws APIManagementException;

    String getTenantConfigSchema(String organization);

    /**
     * Get policy list for given level
     *
     * @param tenantId  tenant ID of user
     * @param level     policy level
     * @return
     * @throws APIManagementException
     */
    Policy[] getPolicies(int tenantId, String level) throws APIManagementException;

    Policy getPolicyByNameAndType(int tenantId, String level, String name) throws APIManagementException;

    /**
     * Update the Api Provider of a given Api Id
     *
     * @param apiId Api ID
     * @param provider New ProviderName/Owner of the Api
     * @param organisation Organisation
     * @throws APIManagementException
     */
    void updateApiProvider(String apiId, String provider, String organisation) throws APIManagementException;

    /**
     * Get/Search All Apis in admin portal
     *
     * @param searchQuery Api name search query
     * @param organization organization
     * @param start
     * @param end
     * @return
     * @throws APIManagementException
     */
    Map<String, Object> searchPaginatedApis(String searchQuery, String organization, int start, int end)
            throws APIManagementException;

    /**
     * This method used to retrieve global key manager configurations
     * @return KeyManagerConfigurationDTO list
     * @throws APIManagementException if error occurred
     */
    List<KeyManagerConfigurationDTO> getGlobalKeyManagerConfigurations() throws APIManagementException;

    /**
     * This method used to retrieve global key manager configurations with usage check
     * @param organization organization
     * @return KeyManagerConfigurationDTO list
     * @throws APIManagementException if error occurred
     */
    List<KeyManagerConfigurationDTO> getGlobalKeyManagerConfigurations(String organization)
            throws APIManagementException;

    /**
     * This method used to retrieve global key manager with Id
     * @param id uuid of key manager
     * @return KeyManagerConfigurationDTO for retrieved data
     * @throws APIManagementException
     */
    KeyManagerConfigurationDTO getGlobalKeyManagerConfigurationById(String id) throws APIManagementException;

    /**
     * This method used to delete global key manager
     * @param id uuid of key manager
     * @throws APIManagementException
     */
    void deleteGlobalKeyManagerConfigurationById(String id) throws APIManagementException;

    /**
     * Adds a new LLM Provider for the given organization.
     *
     * @param organization the organization name to filter
     * @param provider The LLM Provider to add.
     * @return The added LLM Provider.
     * @throws APIManagementException If adding fails.
     */
    LLMProvider addLLMProvider(String organization, LLMProvider provider) throws APIManagementException;

    /**
     * Retrieves a list of LLM providers based on the given filters.
     *
     * @param organization the organization name to filter
     * @param name the provider name to filter
     * @param apiVersion the API version to filter
     * @param builtInSupport whether to filter
     * @return a list of LLM providers matching the specified filters
     * @throws APIManagementException if an error occurs while retrieving the providers
     */
    List<LLMProvider> getLLMProviders(String organization, String name, String apiVersion, Boolean builtInSupport) throws APIManagementException;

    /**
     * Retrieves the list of models registered under the LLM provider
     *
     * @param organization  The organization name
     * @param llmProviderId The ID of the LLM provider
     * @return list of models registered under the LLM provider
     * @throws APIManagementException If an error occurs while retrieving the models
     */
    List<String> getLLMProviderModels(String organization, String llmProviderId) throws APIManagementException;

    /**
     * Deletes an LLM Provider by ID for the given organization.
     *
     * @param organization The organization name.
     * @param provider     LLM Provider.
     * @param builtIn      Whether the provider is built-in.
     * @return Deleted LLM Provider UUID.
     * @throws APIManagementException If deletion fails.
     */
    String deleteLLMProvider(String organization, LLMProvider provider, boolean builtIn) throws APIManagementException;

    /**
     * Updates an LLM Provider for the given organization.
     *
     * @param organization  The organization name.
     * @param provider The LLM Provider with updated data.
     * @return Updated LLM Provider.
     * @throws APIManagementException If update fails.
     */
    LLMProvider updateLLMProvider(String organization, LLMProvider provider) throws APIManagementException;

    /**
     * Retrieves an LLM Provider by ID for the given organization.
     *
     * @param organization  The organization name.
     * @param llmProviderId The ID of the LLM Provider.
     * @return The LLM Provider.
     * @throws APIManagementException If retrieval fails.
     */
    LLMProvider getLLMProvider(String organization, String llmProviderId) throws APIManagementException;

    /**
     * 
     * Retrieves list of organizations available for the given parent organization.
     * 
     * @param parentOrgId parent organization id
     * @param tenantDomain super domain
     * @return organization list
     */
    List<OrganizationDetailsDTO> getOrganizations(String parentOrgId, String tenantDomain) throws APIManagementException;

    /**
     * Add new organization 
     * @param orgDto organization details
     * @param parentOrgId organization id of the parent
     * @param tenantDomain super organization domain
     * @return organization details
     * @throws APIManagementException
     */
    OrganizationDetailsDTO addOrganization(OrganizationDetailsDTO orgDto, String parentOrgId, String tenantDomain)
            throws APIManagementException;

    /**
     * Get organization details 
     * @param organizationId organization id
     * @param tenantDomain tenant domain
     * @return
     * @throws APIManagementException
     */
    OrganizationDetailsDTO getOrganizationDetails(String organizationId, String tenantDomain)
            throws APIManagementException;

    /**
     * Update organization details 
     * @param organizationInfoDTO organization details
     * @param parentOrgId organization id of the parent
     * @param tenantDomain uper organization domain
     * @return updated organization details
     * @throws APIManagementException
     */
    OrganizationDetailsDTO updateOrganization(OrganizationDetailsDTO organizationInfoDTO, String parentOrgId,
            String tenantDomain) throws APIManagementException;

    /**
     * Delete organization
     * @param organizationId organization
     * @param tenantDomain tenantDomain
     */
    void deleteOrganization(String organizationId, String tenantDomain) throws APIManagementException;

}
