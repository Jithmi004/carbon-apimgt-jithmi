/*
 *  Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.wso2.carbon.apimgt.persistence.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.persistence.APIConstants;
import org.wso2.carbon.apimgt.persistence.RegistryPersistenceImpl;
import org.wso2.carbon.apimgt.persistence.dto.UserContext;
import org.wso2.carbon.apimgt.persistence.exceptions.APIPersistenceException;
import org.wso2.carbon.registry.indexing.RegistryConfigLoader;
import org.wso2.carbon.registry.indexing.indexer.Indexer;

import static org.wso2.carbon.apimgt.persistence.APIConstants.API_GLOBAL_VISIBILITY;
import static org.wso2.carbon.apimgt.persistence.APIConstants.API_OVERVIEW_KEY_MANAGERS;
import static org.wso2.carbon.apimgt.persistence.APIConstants.API_OVERVIEW_VISIBILITY;
import static org.wso2.carbon.apimgt.persistence.APIConstants.APPLICATION_JSON_MEDIA_TYPE;

public class RegistrySearchUtil {

    public static final String TAG_SEARCH_TYPE_PREFIX = "tag";
    public static final String TAG_COLON_SEARCH_TYPE_PREFIX = "tag:";
    public static final String CONTENT_SEARCH_TYPE_PREFIX = "content";
    public static final String DOCUMENTATION_SEARCH_TYPE_PREFIX = "doc";
    public static final String DOCUMENTATION_SEARCH_TYPE_PREFIX_WITH_EQUALS = "doc=";
    public static final String SEARCH_AND_TAG = "&";
    public static final String TAGS_SEARCH_TYPE_PREFIX = "tags";
    public static final String NAME_TYPE_PREFIX = "name";
    public static final String AND_WITH_SPACES = " AND ";
    public static final String API_STATUS = "STATUS";
    public static final String API_PROVIDER = "Provider";
    public static final String DOCUMENT_INDEXER = "org.wso2.carbon.apimgt.impl.indexing.indexer.DocumentIndexer";
    public static final String REST_ASYNC_API_DEFINITION_INDEXER = "org.wso2.carbon.apimgt.impl.indexing.indexer" +
            ".RESTAsyncAPIDefinitionIndexer";
    public static final String GRAPHQL_DEFINITION_INDEXER = "org.wso2.carbon.apimgt.impl.indexing.indexer" +
            ".GraphQLAPIDefinitionIndexer";
    public static final String SOAP_DEFINITION_INDEXER = "org.wso2.carbon.apimgt.impl.indexing.indexer" +
            ".SOAPAPIDefinitionIndexer";
    public static final String STORE_VIEW_ROLES = "store_view_roles";
    public static final String STORE_VIEW_ROLES_FIELD = "store_view_roles_ss:";
    public static final String VISIBLE_ORGANIZATIONS = "visible_organizations";
    public static final String VISIBLE_ORGANIZATIONS_FIELD = "visible_organizations_ss:";
    public static final String PUBLISHER_ROLES = "publisher_roles";
    public static final String DOCUMENT_MEDIA_TYPE_KEY = "application/vnd.wso2-document\\+xml";
    public static final String API_DEF_MEDIA_TYPE_KEY = "application/json";
    public static final String GRAPHQL_DEF_MEDIA_TYPE_KEY = "text/plain(.)+charset=ISO-8859-1";
    public static final String SOAP_DEF_MEDIA_TYPE_KEY = "application/wsdl\\+xml|application/octet-stream";
    public static final String SEARCH_MEDIA_TYPE_FIELD = "mediaType";
    public static final String DOCUMENTATION_INLINE_CONTENT_TYPE = "text/plain";
    public static final String API_RXT_MEDIA_TYPE = "application/vnd.wso2-api+xml";
    public static final String LCSTATE_SEARCH_KEY = "lcState";
    public static final String DOCUMENT_RXT_MEDIA_TYPE = "application/vnd.wso2-document+xml";
    public static final String GRAPHQL_DEFINITION_MEDIA_TYPE = "text/plain; charset=ISO-8859-1";
    public static final String SOAP_DEFINITION_WSDL_XML_MEDIA_TYPE = "application/wsdl+xml";
    public static final String SOAP_DEFINITION_WSDL_FILE_MEDIA_TYPE = "application/octet-stream";

    public static final String API_OVERVIEW_STATUS = "overview_status";
    public static final String API_RELATED_CUSTOM_PROPERTIES_PREFIX = "api_meta.";
    public static final String API_RELATED_CUSTOM_PROPERTIES_DISPLAY_DEV = "__display";
    public static final String LABEL_SEARCH_TYPE_PREFIX = "label";
    public static final String API_LABELS_GATEWAY_LABELS = "labels_labelName";
    private static final String PROVIDER_SEARCH_TYPE_PREFIX = "provider";
    private static final String VERSION_SEARCH_TYPE_PREFIX = "version";
    private static final String CONTEXT_SEARCH_TYPE_PREFIX = "context";
    private static final String CONTEXT_TEMPLATE_SEARCH_TYPE_PREFIX = "contextTemplate";
    public static final String API_DESCRIPTION = "Description";
    public static final String TYPE_SEARCH_TYPE_PREFIX = "type";
    public static final String CATEGORY_SEARCH_TYPE_PREFIX = "api-category";
    public static final String ADVERTISE_ONLY_SEARCH_TYPE_PREFIX = "thirdParty";
    public static final String ADVERTISE_ONLY_ADVERTISED_PROPERTY = "advertiseOnly";
    public static final String ENABLE_STORE = "enableStore";
    public static final String API_CATEGORIES_CATEGORY_NAME = "apiCategories_categoryName";
    public static final String NULL_USER_ROLE_LIST = "null";
    public static final String GET_API_PRODUCT_QUERY  = "type=APIProduct";
    public static final String ENDPOINT_CONFIG_SEARCH_TYPE_PREFIX  = "endpointConfig";
    public static final String[] API_SEARCH_PREFIXES = { ENDPOINT_CONFIG_SEARCH_TYPE_PREFIX.toLowerCase(), DOCUMENTATION_SEARCH_TYPE_PREFIX, TAGS_SEARCH_TYPE_PREFIX,
            NAME_TYPE_PREFIX, PROVIDER_SEARCH_TYPE_PREFIX, CONTEXT_SEARCH_TYPE_PREFIX,
            CONTEXT_TEMPLATE_SEARCH_TYPE_PREFIX.toLowerCase(), VERSION_SEARCH_TYPE_PREFIX,
            LCSTATE_SEARCH_KEY.toLowerCase(), API_DESCRIPTION.toLowerCase(), API_STATUS.toLowerCase(),
            CONTENT_SEARCH_TYPE_PREFIX, TYPE_SEARCH_TYPE_PREFIX, LABEL_SEARCH_TYPE_PREFIX, CATEGORY_SEARCH_TYPE_PREFIX,
            ENABLE_STORE.toLowerCase() , ADVERTISE_ONLY_SEARCH_TYPE_PREFIX.toLowerCase(), "sort", "group", "group.sort"
            , "group.field", "group.ngroups", "group.format" };
    

    private static final Log log = LogFactory.getLog(RegistryPersistenceImpl.class);


    /**
     * @param inputSearchQuery search Query
     * @return Reconstructed new search query
     * @throws APIManagementException If there is an error in the search query
     */
    private static String constructQueryWithProvidedCriterias(String inputSearchQuery) throws APIPersistenceException {
        String newSearchQuery = "";

        // for empty search query this method should return name=* as the new search query
        // or if it is a content search query, we should not split in spaces, but return as content=*search query*
        // for example.
        if (StringUtils.isEmpty(inputSearchQuery) || (inputSearchQuery.contains(
                CONTENT_SEARCH_TYPE_PREFIX) && inputSearchQuery.split(":").length == 2)) {
            newSearchQuery = getSingleSearchCriteria(inputSearchQuery);
        } else {
            String[] criterea = inputSearchQuery.split(" ");
            criterea = processInput(criterea);
            Map<String, List<String>> critereaMap = new HashMap<>();
            List<String> untaggedContent = new ArrayList();
            for (int i = 0; i < criterea.length; i++) {
                if (criterea[i].contains(":") && criterea[i].split(":").length > 1) {
                    String searchPrefix = criterea[i].split(":")[0];
                    String searchValue = criterea[i].split(":")[1];

                    List<String> values = critereaMap.containsKey(searchPrefix) ?
                            critereaMap.get(searchPrefix) :
                            new ArrayList<>();
                    values.add(searchValue);
                    critereaMap.put(searchPrefix, values);
                } else {
                    untaggedContent.add(criterea[i]);
                }
            }

            // doc content doesn't support AND search
            if (critereaMap.size() > 1 && critereaMap.containsKey(DOCUMENTATION_SEARCH_TYPE_PREFIX)) {
                throw new APIPersistenceException(
                        "Invalid query. AND based search is not supported for " + "doc prefix");
            }

            // When multiple values are present for the same search key those are considered as an OR based search.
            // ex: tags:sales tags:dev -> tags=(sales OR dev)
            // When multiple search keys are present those are considered as an AND based search.
            // ex: name:pizzashack version:1.0 -> name=pizzashack AND version=1.0
            for (Map.Entry<String, List<String>> entry : critereaMap.entrySet()) {
                String nextCriterea = "";
                if (entry.getValue().size() > 1) {
                    if (TAG_SEARCH_TYPE_PREFIX.equals(entry.getKey()) ||
                            TAGS_SEARCH_TYPE_PREFIX.equals(entry.getKey())) {
                        List<String> updatedValues = entry.getValue().stream()
                                .map(value -> value.replace(" ", "\\ "))
                                .collect(Collectors.toList());
                        entry.setValue(updatedValues);
                    }

                    nextCriterea = entry.getKey() + "=" + getORBasedSearchCriteria(
                            entry.getValue().toArray(new String[0]));
                } else {
                    nextCriterea = getSingleSearchCriteria(entry.getKey() + ":" + entry.getValue().get(0));
                }

                newSearchQuery = StringUtils.isNotEmpty(newSearchQuery) ?
                        (newSearchQuery + SEARCH_AND_TAG + nextCriterea) :
                        nextCriterea;
            }
            if (!untaggedContent.isEmpty()) {
                for (String searchCriteria : untaggedContent) {
                    newSearchQuery = StringUtils.isNotEmpty(newSearchQuery) ?
                            (newSearchQuery + SEARCH_AND_TAG + getSingleSearchCriteria(searchCriteria)) :
                            getSingleSearchCriteria(searchCriteria);
                }
            }
        }

        return newSearchQuery;
    }

    /**
     * Processes an input array of strings by grouping related elements.
     *
     * This method consolidates input strings by combining elements around delimiter strings
     * containing a colon (':'), creating a new array where each entry represents a consolidated group.
     *
     * @param input An array of strings to be processed
     * @return A processed array of strings where related elements are grouped together
     *
     * Key behaviors:
     * - Identifies delimiter strings containing a colon
     * - Aggregates subsequent strings with delimiter entries
     * - Trims whitespace from consolidated entries
     *
     * Example:
     * Input:  ["tag:Sample", "APIs", "-", "New", "name:Google"]
     * Output: ["tag:Sample APIs - New ", "name:Google"]
     */
    private static String[] processInput(String[] input) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for (String element : input) {
            if (element.contains(":")) {
                if (current.length() > 0) {
                    result.add(current.toString().trim());
                    current.setLength(0);
                }
                current.append(element);
            } else {
                current.append(" ").append(element);
            }
        }

        if (current.length() > 0) {
            result.add(current.toString().trim());
        }

        return result.toArray(new String[0]);
    }

    /**
     * Generates solr compatible search criteria synatax from user entered query criteria.
     * Ex: From version:1.0.0, this returns version=*1.0.0*
     *
     * @param criteria
     * @return solar compatible criteria
     * @throws APIManagementException
     */
    private static String getSingleSearchCriteria(String criteria) throws APIPersistenceException {

        criteria = criteria.trim();
        String searchValue = criteria;
        String searchKey = NAME_TYPE_PREFIX;

        if (criteria.contains(":")) {
            if (criteria.split(":").length > 1) {
                String[] splitValues = criteria.split(":");
                searchKey = splitValues[0].trim();
                searchValue = splitValues[1];
                // if search key is 'tag' instead of 'tags', allow it as well since rest api document says query
                // param to use for tag search is 'tag'

                if (TAG_SEARCH_TYPE_PREFIX.equals(searchKey) || TAGS_SEARCH_TYPE_PREFIX.equals(searchKey)) {
                    searchKey = TAGS_SEARCH_TYPE_PREFIX;
                    searchValue = searchValue.replace(" ", "\\ ");
                }

                if (!DOCUMENTATION_SEARCH_TYPE_PREFIX.equalsIgnoreCase(searchKey)
                        && !TAGS_SEARCH_TYPE_PREFIX.equalsIgnoreCase(searchKey)) {
                    if (API_STATUS.equalsIgnoreCase(searchKey)) {
                        searchValue = searchValue.toLowerCase();
                    }
                    if (!(searchValue.endsWith("\"") && searchValue.startsWith("\""))) {
                        if (!searchValue.endsWith("*")) {
                            searchValue = searchValue + "*";
                        }
                        if (!searchValue.startsWith("*")) {
                            searchValue = "*" + searchValue;
                        }
                    } else {
                        if (CONTEXT_SEARCH_TYPE_PREFIX.equalsIgnoreCase(searchKey)) {
                            //Remove quotation marks and forward slash to get the context for exact search.
                            searchValue = searchValue.substring(1, searchValue.length() - 1);
                            if (searchValue.startsWith("/")) {
                                searchValue = searchValue.substring(1);
                            }
                            if (searchValue.endsWith("/")) {
                                searchValue = searchValue.substring(0, searchValue.length() - 1);
                            }
                            if (!searchValue.isEmpty()) {
                                searchValue = "(*\\/" + searchValue + "\\/*" + " OR " + "\\/" + searchValue + ")";
                            } else {
                                searchValue = "\"\"";
                            }
                        }
                    }
                }

            } else {
                throw new APIPersistenceException("Search term is missing. Try again with valid search query.");
            }
        } else {
            if (!(searchValue.endsWith("\"") && searchValue.startsWith("\""))) {
                if (!searchValue.endsWith("*")) {
                    searchValue = searchValue + "*";
                }
                if (!searchValue.startsWith("*")) {
                    searchValue = "*" + searchValue;
                }
            }
        }
        if (API_PROVIDER.equalsIgnoreCase(searchKey)) {
            searchValue = searchValue.replaceAll("@", "-AT-");
        }
        return searchKey + "=" + searchValue;
    }

    private static Map<String, String> getSearchAttributes(String searchQuery) {
        String[] searchQueries = searchQuery.split("&");

        String apiState = "";
        String publisherRoles = "";
        Map<String, String> attributes = new HashMap<String, String>();
        String devportalFilterQuery = "";
        String devportalFilterQueryField = "";
        for (String searchCriterea : searchQueries) {
            String[] keyVal = searchCriterea.split("=");
            if (STORE_VIEW_ROLES.equals(keyVal[0])) {
                if (!StringUtils.isEmpty(keyVal[1])) {
                    if (StringUtils.isEmpty(devportalFilterQuery)) {
                        devportalFilterQueryField = STORE_VIEW_ROLES;
                        devportalFilterQuery = keyVal[1];
                    } else {
                        devportalFilterQuery += (AND_WITH_SPACES + STORE_VIEW_ROLES_FIELD + keyVal[1]);
                    }
                }
            } else if (VISIBLE_ORGANIZATIONS.equals(keyVal[0])) {
                if (!StringUtils.isEmpty(keyVal[1])) {
                    if (StringUtils.isEmpty(devportalFilterQuery)) {
                        devportalFilterQueryField = VISIBLE_ORGANIZATIONS;
                        devportalFilterQuery = keyVal[1];
                    } else {
                        devportalFilterQuery += (AND_WITH_SPACES + VISIBLE_ORGANIZATIONS_FIELD + keyVal[1]);
                    }
                }
            } else if (PUBLISHER_ROLES.equals(keyVal[0])) {
                publisherRoles = keyVal[1];
            } else {
                if (LCSTATE_SEARCH_KEY.equals(keyVal[0])) {
                    apiState = keyVal[1];
                    continue;
                }
                keyVal[1] = keyVal[1].replaceAll(" ", "&&");
                attributes.put(keyVal[0], keyVal[1]);
            }
        }
        if (!StringUtils.isEmpty(devportalFilterQueryField)) {
            attributes.put("propertyName", devportalFilterQueryField);
            attributes.put("rightPropertyValue", devportalFilterQuery);
            attributes.put("rightOp", "eq");
        }

        //check whether the new document indexer is engaged
        RegistryConfigLoader registryConfig = RegistryConfigLoader.getInstance();
        Map<String, Indexer> indexerMap = registryConfig.getIndexerMap();
        Indexer documentIndexer = indexerMap.get(DOCUMENT_MEDIA_TYPE_KEY);
        Indexer jsonIndexer = indexerMap.get(API_DEF_MEDIA_TYPE_KEY);
        Indexer graphqlIndexer = indexerMap.get(GRAPHQL_DEF_MEDIA_TYPE_KEY);
        Indexer soapIndexer = indexerMap.get(SOAP_DEF_MEDIA_TYPE_KEY);
        String complexAttribute = ClientUtils.escapeQueryChars(API_RXT_MEDIA_TYPE);
        if (!StringUtils.isEmpty(publisherRoles)) {
            complexAttribute =
                    "(" + ClientUtils.escapeQueryChars(API_RXT_MEDIA_TYPE) + " AND publisher_roles_ss:"
                            + publisherRoles + ")";
        }

        if (documentIndexer != null && DOCUMENT_INDEXER.equals(documentIndexer.getClass().getName())) {
            if (!StringUtils.isEmpty(publisherRoles)) {
                complexAttribute += " OR mediaType_s:(" + ClientUtils
                        .escapeQueryChars(DOCUMENT_RXT_MEDIA_TYPE) + " AND publisher_roles_s:" + publisherRoles + ")";
            } else {
                complexAttribute += " OR mediaType_s:(" + ClientUtils
                        .escapeQueryChars(DOCUMENT_RXT_MEDIA_TYPE) + " AND document_indexed_s:true)";
            }
        }

        if (jsonIndexer != null && REST_ASYNC_API_DEFINITION_INDEXER.equals(jsonIndexer.getClass().getName())) {
            if (!StringUtils.isEmpty(publisherRoles)) {
                complexAttribute += " OR mediaType_s:(" + ClientUtils
                        .escapeQueryChars(APPLICATION_JSON_MEDIA_TYPE) + " AND publisher_roles_s:" + publisherRoles + ")";
            } else {
                complexAttribute += " OR mediaType_s:(" + ClientUtils
                        .escapeQueryChars(APPLICATION_JSON_MEDIA_TYPE) + " AND document_indexed_s:true)";
            }
        }

        if (graphqlIndexer != null && GRAPHQL_DEFINITION_INDEXER.equals(graphqlIndexer.getClass().getName())) {
            if (!StringUtils.isEmpty(publisherRoles)) {
                complexAttribute += " OR mediaType_s:(" + ClientUtils
                        .escapeQueryChars(GRAPHQL_DEFINITION_MEDIA_TYPE) + " AND publisher_roles_s:" + publisherRoles + ")";
            } else {
                complexAttribute += " OR mediaType_s:(" + ClientUtils
                        .escapeQueryChars(GRAPHQL_DEFINITION_MEDIA_TYPE) + " AND document_indexed_s:true)";
            }
        }

        if (soapIndexer != null && SOAP_DEFINITION_INDEXER.equals(soapIndexer.getClass().getName())) {
            if (!StringUtils.isEmpty(publisherRoles)) {
                complexAttribute += " OR mediaType_s:((" + ClientUtils
                        .escapeQueryChars(SOAP_DEFINITION_WSDL_XML_MEDIA_TYPE) +
                        " OR " + ClientUtils.escapeQueryChars(SOAP_DEFINITION_WSDL_FILE_MEDIA_TYPE)
                        + " ) AND publisher_roles_s:" + publisherRoles + ")";
            } else {
                complexAttribute += " OR mediaType_s:((" + ClientUtils
                        .escapeQueryChars(SOAP_DEFINITION_WSDL_XML_MEDIA_TYPE) +
                        " OR " + ClientUtils.escapeQueryChars(SOAP_DEFINITION_WSDL_FILE_MEDIA_TYPE)
                        + " ) AND document_indexed_s:true)";
            }
        }

        attributes.put(SEARCH_MEDIA_TYPE_FIELD, complexAttribute);
        attributes.put(API_OVERVIEW_STATUS, apiState);
        return attributes;
    }
    

    private static String extractQuery(String searchQuery, boolean isDevPortal) {
        String[] searchQueries = searchQuery.split("&");
        StringBuilder filteredQuery = new StringBuilder();

        // Filtering the queries related with custom properties
        for (String query : searchQueries) {
            if (searchQuery.startsWith(DOCUMENTATION_SEARCH_TYPE_PREFIX)) {
                filteredQuery.append(query);
                break;
            }
            // If the query does not contain "=" then it is an erroneous scenario.
            if (query.contains("=")) {
                String[] searchKeys = query.split("=");

                if (searchKeys.length >= 2) {
                    if (!Arrays.asList(API_SEARCH_PREFIXES).contains(searchKeys[0].toLowerCase())) {
                        if (log.isDebugEnabled()) {
                            log.debug(searchKeys[0] + " does not match with any of the reserved key words. Hence"
                                    + " appending " + API_RELATED_CUSTOM_PROPERTIES_PREFIX + " as prefix");
                        }
                        if (isDevPortal) {
                            searchKeys[0] = (API_RELATED_CUSTOM_PROPERTIES_PREFIX + searchKeys[0]
                                    + API_RELATED_CUSTOM_PROPERTIES_DISPLAY_DEV);
                        } else {
                            searchKeys[0] = (API_RELATED_CUSTOM_PROPERTIES_PREFIX + searchKeys[0]);
                        }

                    }

                    // Ideally query keys for label and  category searchs are as below
                    //      label -> labels_labelName
                    //      category -> apiCategories_categoryName
                    // Since these are not user friendly we allow to use prefixes label and api-category. And label and
                    // category search should only return results that exactly match.
                    if (searchKeys[0].equals(LABEL_SEARCH_TYPE_PREFIX)) {
                        searchKeys[0] = API_LABELS_GATEWAY_LABELS;
                        searchKeys[1] = searchKeys[1].replace("*", "");
                    } else if (searchKeys[0].equals(CATEGORY_SEARCH_TYPE_PREFIX)) {
                        searchKeys[0] = API_CATEGORIES_CATEGORY_NAME;
                        searchKeys[1] = searchKeys[1].replace("*", "");
                    } else if (searchKeys[0].equals(ADVERTISE_ONLY_SEARCH_TYPE_PREFIX)) {
                        searchKeys[0] = ADVERTISE_ONLY_ADVERTISED_PROPERTY;
                    }

                    if (filteredQuery.length() == 0) {
                        filteredQuery.append(searchKeys[0]).append("=").append(searchKeys[1]);
                    } else {
                        filteredQuery.append("&").append(searchKeys[0]).append("=").append(searchKeys[1]);
                    }
                }
            } else {
                filteredQuery.append(query);
            }
        }
        return filteredQuery.toString();
    }
    
    private static String getPublisherRolesWrappedQuery(String query, UserContext context) {

        if (PersistenceUtil.isAdminUser(context)) {
            log.debug("Admin user. no modifications to the query");
            return query;
        }
        String criteria = PUBLISHER_ROLES + "="
                + getUserRolesQuery(context.getRoles(), PersistenceUtil.getSkipRoles(context));
        if (query != null && !query.trim().isEmpty()) {
            criteria = criteria + "&" + query;
        }
        if (log.isDebugEnabled()) {
            log.debug("User roles wrapped query : " + criteria);
        }

        return criteria;
    }

    private static String getDevPortalVisibilityWrappedQuery(String query, boolean isCrossTenant) {
        if (!isCrossTenant) {
            log.debug("Not a cross tenant scenario");
            return query;
        }
        String criteria = API_OVERVIEW_VISIBILITY + "="
                + API_GLOBAL_VISIBILITY;
        if (query != null && !query.trim().isEmpty()) {
            criteria = criteria + "&" + query;
        }
        if (log.isDebugEnabled()) {
            log.debug("Visibility wrapped query : " + criteria);
        }
        return criteria;
    }

    private static String getDevPortalRolesWrappedQuery(String query, UserContext context) {
        if (PersistenceUtil.isAdminUser(context)) {
            log.debug("Admin user. no modifications to the query");
            return query;
        }
        String criteria = STORE_VIEW_ROLES + "="
                + getUserRolesQuery(context.getRoles(), PersistenceUtil.getSkipRoles(context));
        if (query != null && !query.trim().isEmpty()) {
            criteria = criteria + "&" + query;
        }
        if (log.isDebugEnabled()) {
            log.debug("User roles wrapped query : " + criteria);
        }

        return criteria;
    }

    private static String getOrganizationVisibilityWrappedQuery(String query, UserContext context, String userTenantDomain) {
        if (PersistenceUtil.isAdminUser(context)) {
            log.debug("Admin user. no modifications to the query");
            return query;
        }
        String criteria;
        String orgId = context.getOrganization().getId();

        if (userTenantDomain.equals(context.getOrganization().getName())) {
            criteria = VISIBLE_ORGANIZATIONS + "=" + "(" + APIConstants.DEFAULT_VISIBLE_ORG + " OR " + orgId + " OR "
                    + userTenantDomain + ")";
        } else {
            criteria = VISIBLE_ORGANIZATIONS + "=" + "(" + APIConstants.DEFAULT_VISIBLE_ORG + " OR " + orgId + ")";
        }
        
        if (query != null && !query.trim().isEmpty()) {
            criteria = criteria + "&" + query;
        }
        if (log.isDebugEnabled()) {
            log.debug("Organization visibility wrapped query : " + criteria);
        }
        return criteria;
    }


    private static String getUserRolesQuery(String[] userRoles, String skippedRoles) {

        StringBuilder rolesQuery = new StringBuilder();
        rolesQuery.append('(');
        rolesQuery.append(NULL_USER_ROLE_LIST);
        String skipRolesByRegex = skippedRoles;
        if (StringUtils.isNotEmpty(skipRolesByRegex)) {
            List<String> filteredUserRoles = new ArrayList<>(Arrays.asList(userRoles));
            String[] regexList = skipRolesByRegex.split(",");
            for (int i = 0; i < regexList.length; i++) {
                Pattern p = Pattern.compile(regexList[i]);
                Iterator<String> itr = filteredUserRoles.iterator();
                while(itr.hasNext()) {
                    String role = itr.next();
                    Matcher m = p.matcher(role);
                    if (m.matches()) {
                        itr.remove();
                    }
                }
            }
            userRoles = filteredUserRoles.toArray(new String[0]);
        }
        if (userRoles != null) {
            for (String userRole : userRoles) {
                rolesQuery.append(" OR ");
                rolesQuery.append(ClientUtils.escapeQueryChars(sanitizeUserRole(userRole.toLowerCase())));
            }
        }
        rolesQuery.append(")");
        return rolesQuery.toString();
        
    }
    
    /**
     * Convert special characters to encoded value.
     *
     * @param role
     * @return encorded value
     */
    private static String sanitizeUserRole(String role) {

        if (role.contains("&")) {
            return role.replaceAll("&", "%26");
        } else {
            return role;
        }
    }

    /**
     * Composes OR based search criteria from provided array of values
     *
     * @param values
     * @return
     */
    private static String getORBasedSearchCriteria(String[] values) {

        String criteria = "(";
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                criteria = criteria + values[i];
                if (i != values.length - 1) {
                    criteria = criteria + " OR ";
                } else {
                    criteria = criteria + ")";
                }
            }
            return criteria;
        }
        return null;
    }
    
    public static String getDevPortalSearchQuery(String searchQuery, UserContext ctx, boolean displayMultipleStatus,
                            boolean isAllowDisplayMultipleVersions) throws APIPersistenceException {
        String modifiedQuery = RegistrySearchUtil.constructNewSearchQuery(searchQuery);
        if (!APIConstants.DOCUMENTATION_SEARCH_TYPE_PREFIX_WITH_EQUALS.startsWith(modifiedQuery)) {
            
            String[] statusList = { APIConstants.PUBLISHED, APIConstants.PROTOTYPED };
            if (displayMultipleStatus) {
                statusList = new String[] { APIConstants.PUBLISHED, APIConstants.PROTOTYPED,
                        APIConstants.DEPRECATED };
            }
            if (StringUtils.isEmpty(searchQuery)) { // normal listing
                String enableStoreCriteria = APIConstants.ENABLE_STORE_SEARCH_TYPE_KEY;
                if (isAllowDisplayMultipleVersions) {
                    modifiedQuery = modifiedQuery + APIConstants.SEARCH_AND_TAG + enableStoreCriteria;
                } else {
                    // solr result grouping is used when displayMultipleVersions is not enabled(default).
                    modifiedQuery = modifiedQuery + APIConstants.SEARCH_AND_TAG + enableStoreCriteria +
                            "&group=true&group.field=name&group.ngroups=true&group.sort=versionComparable desc";
                }
            }

            if (!modifiedQuery.startsWith(APIConstants.API_OVERVIEW_STATUS_SEARCH_TYPE_KEY) && !modifiedQuery
                    .contains(APIConstants.SEARCH_AND_TAG + APIConstants.API_OVERVIEW_STATUS_SEARCH_TYPE_KEY)) {

                String apiOverviewStateCriteria = APIConstants.API_OVERVIEW_STATUS_SEARCH_TYPE_KEY;
                apiOverviewStateCriteria = apiOverviewStateCriteria + getORBasedSearchCriteria(statusList);

                modifiedQuery = modifiedQuery + APIConstants.SEARCH_AND_TAG + apiOverviewStateCriteria;
            }
        }
        modifiedQuery = RegistrySearchUtil.getDevPortalRolesWrappedQuery(extractQuery(modifiedQuery, true), ctx);
        return modifiedQuery;
    }

    public static String getPublisherSearchQuery(String searchQuery, UserContext ctx) throws APIPersistenceException {

        // If the context is dynamic, modify the searchQuery to use the templated context instead of plain context
        if (searchQuery.contains("context") && searchQuery.contains("{")) {
            searchQuery = searchQuery.replace(CONTEXT_SEARCH_TYPE_PREFIX, CONTEXT_TEMPLATE_SEARCH_TYPE_PREFIX);
            searchQuery = searchQuery.replace("{", ClientUtils.escapeQueryChars("{"));
            searchQuery = searchQuery.replace("}", ClientUtils.escapeQueryChars("}"));
        }

        String newSearchQuery = constructNewSearchQuery(searchQuery);

        if (!newSearchQuery.contains(APIConstants.TYPE)) {
            String typeCriteria = APIConstants.TYPE_SEARCH_TYPE_KEY
                    + getORBasedSearchCriteria(APIConstants.API_SUPPORTED_TYPE_LIST);
            newSearchQuery = newSearchQuery + APIConstants.SEARCH_AND_TAG + typeCriteria;
        }
        newSearchQuery = extractQuery(newSearchQuery, false);

        newSearchQuery = RegistrySearchUtil.getPublisherRolesWrappedQuery(newSearchQuery, ctx);
        return newSearchQuery;
    }

    public static String getPublisherProductSearchQuery(String searchQuery, UserContext ctx)
            throws APIPersistenceException {
        //for now one criterea is supported
        String newSearchQuery = StringUtils.replace(searchQuery, ":", "=");
        newSearchQuery = searchQuery.equals("") ? GET_API_PRODUCT_QUERY : searchQuery + SEARCH_AND_TAG +
                GET_API_PRODUCT_QUERY;

        newSearchQuery = RegistrySearchUtil.getPublisherRolesWrappedQuery(newSearchQuery, ctx);
        return newSearchQuery;
    }
    
    /**
     * Used to reconstruct the input search query as sub context and doc content doesn't support AND search
     *
     * @param query Input search query
     * @return Reconstructed new search query
     * @throws APIManagementException If there is an error in the search query
     */
    private static String constructNewSearchQuery(String query) throws APIPersistenceException {

        return constructQueryWithProvidedCriterias(query.trim());
    }

    
    public static Map<String, String> getDevPortalSearchAttributes(String searchQuery, UserContext ctx,
           boolean isCrossTenant, boolean displayMultipleStatus, String userTenantDomain) throws APIPersistenceException {
        String modifiedQuery = RegistrySearchUtil.constructNewSearchQuery(searchQuery);

        if (!(StringUtils.containsIgnoreCase(modifiedQuery, APIConstants.API_STATUS))) {
            String[] statusList = { APIConstants.PUBLISHED.toLowerCase(), APIConstants.PROTOTYPED.toLowerCase(),
                    "null" };
            if (displayMultipleStatus) {
                statusList = new String[] { APIConstants.PUBLISHED.toLowerCase(), APIConstants.PROTOTYPED.toLowerCase(),
                        APIConstants.DEPRECATED.toLowerCase(), "null" };
            }
            String lcCriteria = APIConstants.LCSTATE_SEARCH_TYPE_KEY
                    + RegistrySearchUtil.getORBasedSearchCriteria(statusList);
            modifiedQuery = modifiedQuery + APIConstants.SEARCH_AND_TAG + lcCriteria;
        } else {
            String searchString = APIConstants.API_STATUS + "=";
            modifiedQuery = StringUtils.replaceIgnoreCase(modifiedQuery, searchString,
                    APIConstants.LCSTATE_SEARCH_TYPE_KEY);
        }
        if (PersistenceUtil.areOrganizationsRegistered(ctx)) {
            modifiedQuery = RegistrySearchUtil.getOrganizationVisibilityWrappedQuery(modifiedQuery, ctx, userTenantDomain);
        }
        modifiedQuery = RegistrySearchUtil.getDevPortalRolesWrappedQuery(modifiedQuery, ctx);
        modifiedQuery = RegistrySearchUtil.getDevPortalVisibilityWrappedQuery(modifiedQuery, isCrossTenant);
        Map<String, String> attributes = RegistrySearchUtil.getSearchAttributes(modifiedQuery);
        return attributes;
    }
    

    public static Map<String, String> getPublisherSearchAttributes(String searchQuery, UserContext ctx)
            throws APIPersistenceException {
        String modifiedQuery = RegistrySearchUtil.constructNewSearchQuery(searchQuery);
        modifiedQuery = RegistrySearchUtil.getPublisherRolesWrappedQuery(modifiedQuery, ctx);
        Map<String, String> attributes = RegistrySearchUtil.getSearchAttributes(modifiedQuery);
        return attributes;
    }

    public static Map<String, String> getAdminSearchAttributes(String searchQuery) {
        Map<String, String> attributes = new HashMap<String, String>();
        if (searchQuery.equals(APIConstants.CHAR_ASTERIX)) {
            String modifiedQuery = APIConstants.API_OVERVIEW_NAME + "=" + APIConstants.CHAR_ASTERIX;
            attributes = RegistrySearchUtil.getSearchAttributes(modifiedQuery);
        } else if (searchQuery.startsWith(API_OVERVIEW_KEY_MANAGERS + ":")) {
            String[] queryParts = searchQuery.split(":");
            String name = queryParts.length > 1 ? queryParts[1] : "";
            attributes.put(API_OVERVIEW_KEY_MANAGERS, name);
            attributes.put(APIConstants.DOCUMENTATION_SEARCH_MEDIA_TYPE_FIELD, API_RXT_MEDIA_TYPE);
        } else {
            searchQuery = searchQuery.replaceAll(" ", "\\\\ ");
            attributes.put(APIConstants.DOCUMENTATION_SEARCH_MEDIA_TYPE_FIELD, API_RXT_MEDIA_TYPE);
            attributes.put(APIConstants.API_OVERVIEW_NAME, searchQuery);
            attributes.put(APIConstants.API_OVERVIEW_TYPE,
                    "(http OR ws OR soaptorest OR graphql OR soap OR sse OR websub OR webhook OR async)");
        }
        return attributes;
    }
}
