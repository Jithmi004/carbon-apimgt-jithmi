/*
*  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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


package org.wso2.carbon.apimgt.impl.dao.constants;

/**
 * This class will hold DB2 quires.
 */
public class SQLConstantsDB2 extends SQLConstants{



    public static final String GET_APPLICATIONS_PREFIX_CASESENSITVE_WITHGROUPID =
            "select distinct x.*,bl.ENABLED from (" +
                    "SELECT * FROM (" +
                    "   SELECT " +
                    "   ROW_NUMBER() OVER (ORDER BY APPLICATION_ID) as row ," +
                    "   APPLICATION_ID, " +
                    "   NAME," +
                    "   APPLICATION_TIER," +
                    "   APP.SUBSCRIBER_ID,  " +
                    "   APP.CREATED_TIME AS APP_CREATED_TIME,  " +
                    "   APP.UPDATED_TIME AS APP_UPDATED_TIME,  " +
                    "   CALLBACK_URL,  " +
                    "   DESCRIPTION, " +
                    "   APPLICATION_STATUS, " +
                    "   USER_ID, " +
                    "   GROUP_ID, " +
                    "   UUID, " +
                    "   APP.CREATED_BY AS CREATED_BY, " +
                    "   APP.TOKEN_TYPE AS TOKEN_TYPE, " +
                    "   APP.SHARED_ORGANIZATION AS SHARED_ORGANIZATION " +
                    " FROM" +
                    "   AM_APPLICATION APP, " +
                    "   AM_SUBSCRIBER SUB  " +
                    " WHERE " +
                    "   SUB.SUBSCRIBER_ID = APP.SUBSCRIBER_ID " +
                    " AND " +
                    "   (GROUP_ID= ?  OR  (GROUP_ID='' AND SUB.USER_ID = ?))" +
                    " AND " +
                    "   APP.ORGANIZATION = ? " +
                    " And " +
                    "    LOWER (NAME) like LOWER (?)" +
                    " ) a )x left join AM_BLOCK_CONDITIONS bl on  ( bl.TYPE = 'APPLICATION' AND bl.BLOCK_CONDITION = concat(concat(x.USER_ID,':'),x.name)) " +
                    " ORDER BY $1 $2 limit ? , ? ";




    public static final String GET_APPLICATIONS_PREFIX_NONE_CASESENSITVE_WITHGROUPID =
            "select distinct x.*,bl.ENABLED from (" +
                    "SELECT * FROM (" +
                    "   SELECT " +
                    "   ROW_NUMBER() OVER (ORDER BY APPLICATION_ID) as row," +
                    "   APPLICATION_ID, " +
                    "   NAME," +
                    "   APPLICATION_TIER," +
                    "   APP.SUBSCRIBER_ID,  " +
                    "   APP.CREATED_TIME AS APP_CREATED_TIME, " +
                    "   APP.UPDATED_TIME AS APP_UPDATED_TIME, " +
                    "   CALLBACK_URL,  " +
                    "   DESCRIPTION, " +
                    "   APPLICATION_STATUS, " +
                    "   USER_ID, " +
                    "   GROUP_ID, " +
                    "   UUID, " +
                    "   APP.CREATED_BY AS CREATED_BY, " +
                    "   APP.TOKEN_TYPE AS TOKEN_TYPE, " +
                    "   APP.SHARED_ORGANIZATION AS SHARED_ORGANIZATION " +
                    " FROM" +
                    "   AM_APPLICATION APP, " +
                    "   AM_SUBSCRIBER SUB  " +
                    " WHERE " +
                    "   SUB.SUBSCRIBER_ID = APP.SUBSCRIBER_ID " +
                    " AND " +
                    "   (GROUP_ID= ?  OR (GROUP_ID='' AND LOWER (SUB.USER_ID) = LOWER (?)))"+
                    " AND " +
                    "   APP.ORGANIZATION = ? " +
                    " And "+
                    "    LOWER (NAME) like LOWER (?)" +
                    " ) a )x left join AM_BLOCK_CONDITIONS bl on  ( bl.TYPE = 'APPLICATION' AND bl.BLOCK_CONDITION = concat(concat(x.USER_ID,':'),x.name)) " +
                    " ORDER BY $1 $2 limit ? , ? ";

    public static final String GET_APPLICATIONS_PREFIX_CASESENSITVE =
            "select distinct x.*,bl.ENABLED from (" +
                    "SELECT * FROM (" +
                    "   SELECT " +
                    "   ROW_NUMBER() OVER (ORDER BY APPLICATION_ID) as row," +
                    "   APPLICATION_ID, " +
                    "   NAME," +
                    "   APPLICATION_TIER," +
                    "   APP.SUBSCRIBER_ID,  " +
                    "   APP.CREATED_TIME AS APP_CREATED_TIME, " +
                    "   APP.UPDATED_TIME AS APP_UPDATED_TIME, " +
                    "   CALLBACK_URL,  " +
                    "   DESCRIPTION, " +
                    "   APPLICATION_STATUS, " +
                    "   USER_ID, " +
                    "   GROUP_ID, " +
                    "   UUID, " +
                    "   APP.CREATED_BY AS CREATED_BY, " +
                    "   APP.TOKEN_TYPE AS TOKEN_TYPE, " +
                    "   APP.SHARED_ORGANIZATION AS SHARED_ORGANIZATION " +
                    " FROM" +
                    "   AM_APPLICATION APP, " +
                    "   AM_SUBSCRIBER SUB  " +
                    " WHERE " +
                    "   SUB.SUBSCRIBER_ID = APP.SUBSCRIBER_ID " +
                    " AND " +
                    "    SUB.USER_ID = ?"+
                    " AND " +
                    "   APP.ORGANIZATION = ? " +
                    " And "+
                    "    LOWER (NAME) like LOWER (?)" +
                    " ) a )x left join AM_BLOCK_CONDITIONS bl on  ( bl.TYPE = 'APPLICATION' AND bl.BLOCK_CONDITION = concat(concat(x.USER_ID,':'),x.name)) " +
                    " ORDER BY $1 $2 limit ? , ? ";


    public static final String GET_APPLICATIONS_PREFIX_NONE_CASESENSITVE =
            "select distinct x.*,bl.ENABLED from (" +
                    "SELECT * FROM (" +
                    "   SELECT " +
                    "   ROW_NUMBER() OVER (ORDER BY APPLICATION_ID) as row," +
                    "   APPLICATION_ID, " +
                    "   NAME," +
                    "   APPLICATION_TIER," +
                    "   APP.SUBSCRIBER_ID,  " +
                    "   APP.CREATED_TIME AS APP_CREATED_TIME, " +
                    "   APP.UPDATED_TIME AS APP_UPDATED_TIME, " +
                    "   CALLBACK_URL,  " +
                    "   DESCRIPTION, " +
                    "   APPLICATION_STATUS, " +
                    "   USER_ID, " +
                    "   GROUP_ID, " +
                    "   UUID, " +
                    "   APP.CREATED_BY AS CREATED_BY, " +
                    "   APP.TOKEN_TYPE AS TOKEN_TYPE, " +
                    "   APP.SHARED_ORGANIZATION AS SHARED_ORGANIZATION " +
                    " FROM" +
                    "   AM_APPLICATION APP, " +
                    "   AM_SUBSCRIBER SUB  " +
                    " WHERE " +
                    "   SUB.SUBSCRIBER_ID = APP.SUBSCRIBER_ID " +
                    " AND " +
                    "   LOWER (SUB.USER_ID) = LOWER(?)" +
                    " AND " +
                    "   APP.ORGANIZATION = ? " +
                    " And "+
                    "    LOWER (NAME) like LOWER (?)" +
                    " ) a )x left join AM_BLOCK_CONDITIONS bl on  ( bl.TYPE = 'APPLICATION' AND bl.BLOCK_CONDITION = concat(concat(x.USER_ID,':'),x.name)) " +
                    " ORDER BY $1 $2 limit ? , ? ";

    public static final String GET_APPLICATIONS_PREFIX_NONE_CASESENSITVE_WITH_MULTIGROUPID =
            "select distinct x.*,bl.ENABLED from (" +
                    "SELECT " +
                    "   APPLICATION_ID, " +
                    "   NAME," +
                    "   APPLICATION_TIER," +
                    "   APP.SUBSCRIBER_ID,  " +
                    "   APP.CREATED_TIME AS APP_CREATED_TIME, " +
                    "   APP.UPDATED_TIME AS APP_UPDATED_TIME, " +
                    "   CALLBACK_URL,  " +
                    "   DESCRIPTION, " +
                    "   APPLICATION_STATUS, " +
                    "   USER_ID, " +
                    "   GROUP_ID, " +
                    "   UUID, " +
                    "   APP.CREATED_BY AS CREATED_BY, " +
                    "   APP.TOKEN_TYPE AS TOKEN_TYPE, " +
                    "   APP.SHARED_ORGANIZATION AS SHARED_ORGANIZATION " +
                    " FROM" +
                    "   AM_APPLICATION APP, " +
                    "   AM_SUBSCRIBER SUB  " +
                    " WHERE " +
                    "   SUB.SUBSCRIBER_ID = APP.SUBSCRIBER_ID " +
                    " AND (" +
                    "    (APPLICATION_ID IN ( SELECT APPLICATION_ID FROM AM_APPLICATION_GROUP_MAPPING WHERE GROUP_ID IN ($params) AND TENANT = ?)) " +
                    "           OR " +
                    "    (LOWER (SUB.USER_ID) = LOWER(?))" +
                    "           OR " +
                    "    (APP.APPLICATION_ID IN (SELECT APPLICATION_ID FROM AM_APPLICATION WHERE GROUP_ID = ?))" +
                    " )" +
                    " AND " +
                    "   APP.ORGANIZATION = ? " +
                    " And "+
                    "    LOWER (NAME) like LOWER (?)"+
                    " ORDER BY $1 $2 " +
                    " limit ? , ? "+
                    " )x left join AM_BLOCK_CONDITIONS bl on  ( bl.TYPE = 'APPLICATION' AND bl.BLOCK_CONDITION = concat(concat(x.USER_ID,':'),x.name)) ";

    public static final String GET_APPLICATIONS_PREFIX_CASESENSITVE_WITH_MULTIGROUPID =
            "select distinct x.*,bl.ENABLED from (" +
                    " SELECT " +
                    "   APPLICATION_ID, " +
                    "   NAME," +
                    "   APPLICATION_TIER," +
                    "   APP.SUBSCRIBER_ID,  " +
                    "   APP.CREATED_TIME AS APP_CREATED_TIME, " +
                    "   APP.UPDATED_TIME AS APP_UPDATED_TIME, " +
                    "   CALLBACK_URL,  " +
                    "   DESCRIPTION, " +
                    "   APPLICATION_STATUS, " +
                    "   USER_ID, " +
                    "   GROUP_ID, " +
                    "   UUID, " +
                    "   APP.CREATED_BY AS CREATED_BY, " +
                    "   APP.TOKEN_TYPE AS TOKEN_TYPE, " +
                    "   APP.SHARED_ORGANIZATION AS SHARED_ORGANIZATION " +
                    " FROM" +
                    "   AM_APPLICATION APP, " +
                    "   AM_SUBSCRIBER SUB  " +
                    " WHERE " +
                    "   SUB.SUBSCRIBER_ID = APP.SUBSCRIBER_ID " +
                    " AND (" +
                    "    (APPLICATION_ID IN ( SELECT APPLICATION_ID FROM AM_APPLICATION_GROUP_MAPPING WHERE GROUP_ID " +
                    " IN ($params) AND TENANT = ? ))" +
                    "           OR " +
                    "    (SUB.USER_ID = ?)" +
                    "           OR " +
                    "     (APP.APPLICATION_ID IN (SELECT APPLICATION_ID FROM AM_APPLICATION WHERE GROUP_ID = ?))" +
                    " )" +
                    " AND " +
                    "   APP.ORGANIZATION = ? " +
                    " And " +
                    "    LOWER (NAME) like LOWER (?)"+
                    " )x left join AM_BLOCK_CONDITIONS bl on  ( bl.TYPE = 'APPLICATION' AND bl.BLOCK_CONDITION = concat(concat(x.USER_ID,':'),x.name)) " +
                    " ORDER BY $1 $2 limit ? , ?";

    public static final String GET_APPLICATIONS_PREFIX_CASESENSITVE_WITH_ORGSHARING =
            "select distinct x.*,bl.ENABLED from (" +
                    "SELECT * FROM (" +
                    "   SELECT " +
                    "   ROW_NUMBER() OVER (ORDER BY APPLICATION_ID) as row," +
                    "   APPLICATION_ID, " +
                    "   NAME," +
                    "   APPLICATION_TIER," +
                    "   APP.SUBSCRIBER_ID,  " +
                    "   APP.CREATED_TIME AS APP_CREATED_TIME, " +
                    "   APP.UPDATED_TIME AS APP_UPDATED_TIME, " +
                    "   CALLBACK_URL,  " +
                    "   DESCRIPTION, " +
                    "   APPLICATION_STATUS, " +
                    "   USER_ID, " +
                    "   GROUP_ID, " +
                    "   UUID, " +
                    "   APP.CREATED_BY AS CREATED_BY, " +
                    "   APP.TOKEN_TYPE AS TOKEN_TYPE, " +
                    "   APP.SHARED_ORGANIZATION AS SHARED_ORGANIZATION " +
                    " FROM" +
                    "   AM_APPLICATION APP, " +
                    "   AM_SUBSCRIBER SUB  " +
                    " WHERE " +
                    "   SUB.SUBSCRIBER_ID = APP.SUBSCRIBER_ID " +
                    " AND " +
                    "    (SUB.USER_ID = ? OR APP.SHARED_ORGANIZATION = ?)"+
                    " AND " +
                    "   APP.ORGANIZATION = ? " +
                    " And "+
                    "    LOWER (NAME) like LOWER (?)" +
                    " ) a )x left join AM_BLOCK_CONDITIONS bl on  ( bl.TYPE = 'APPLICATION' AND bl.BLOCK_CONDITION = concat(concat(x.USER_ID,':'),x.name)) " +
                    " ORDER BY $1 $2 limit ? , ? ";

    public static final String GET_APPLICATIONS_PREFIX_NONE_CASESENSITVE_WITH_ORGSHARING =
            "select distinct x.*,bl.ENABLED from (" +
                    "SELECT * FROM (" +
                    "   SELECT " +
                    "   ROW_NUMBER() OVER (ORDER BY APPLICATION_ID) as row," +
                    "   APPLICATION_ID, " +
                    "   NAME," +
                    "   APPLICATION_TIER," +
                    "   APP.SUBSCRIBER_ID,  " +
                    "   APP.CREATED_TIME AS APP_CREATED_TIME, " +
                    "   APP.UPDATED_TIME AS APP_UPDATED_TIME, " +
                    "   CALLBACK_URL,  " +
                    "   DESCRIPTION, " +
                    "   APPLICATION_STATUS, " +
                    "   USER_ID, " +
                    "   GROUP_ID, " +
                    "   UUID, " +
                    "   APP.CREATED_BY AS CREATED_BY, " +
                    "   APP.TOKEN_TYPE AS TOKEN_TYPE, " +
                    "   APP.SHARED_ORGANIZATION AS SHARED_ORGANIZATION " +
                    " FROM" +
                    "   AM_APPLICATION APP, " +
                    "   AM_SUBSCRIBER SUB  " +
                    " WHERE " +
                    "   SUB.SUBSCRIBER_ID = APP.SUBSCRIBER_ID " +
                    " AND " +
                    "   (LOWER (SUB.USER_ID) = LOWER(?) OR APP.SHARED_ORGANIZATION = ?)" +
                    " AND " +
                    "   APP.ORGANIZATION = ? " +
                    " And "+
                    "    LOWER (NAME) like LOWER (?)" +
                    " ) a )x left join AM_BLOCK_CONDITIONS bl on  ( bl.TYPE = 'APPLICATION' AND bl.BLOCK_CONDITION = concat(concat(x.USER_ID,':'),x.name)) " +
                    " ORDER BY $1 $2 limit ? , ? ";

    public static final String GET_APPLICATIONS_BY_TENANT_ID =
            "(" +
                    "SELECT * FROM (" +
                    "   SELECT " +
                    "   ROW_NUMBER() OVER (ORDER BY APPLICATION_ID) as row," +
                    "   APP.APPLICATION_ID as APPLICATION_ID, " +
                    "   SUB.CREATED_BY AS CREATED_BY, " +
                    "   APP.GROUP_ID AS GROUP_ID, " +
                    "   APP.CREATED_TIME AS APP_CREATED_TIME, " +
                    "   APP.UPDATED_TIME AS APP_UPDATED_TIME, " +
                    "   SUB.TENANT_ID AS TENANT_ID, " +
                    "   SUB.SUBSCRIBER_ID AS SUBSCRIBER_ID, " +
                    "   APP.UUID AS UUID," +
                    "   APP.NAME AS NAME," +
                    "   APP.APPLICATION_STATUS as APPLICATION_STATUS  " +
                    " FROM" +
                    "   AM_APPLICATION APP, " +
                    "   AM_SUBSCRIBER SUB  " +
                    " WHERE " +
                    "   SUB.SUBSCRIBER_ID = APP.SUBSCRIBER_ID " +
                    " AND " +
                    "    SUB.TENANT_ID = ? "+
                    " And "+
                    "    ( SUB.CREATED_BY like ?"+
                    " OR APP.NAME like ?"+
                    " )) a ) " +
                    " ORDER BY $1 $2 limit ? , ? ";

    public static final String GET_REPLIES_SQL =
            "SELECT " +
                "AM_API_COMMENTS.COMMENT_ID, " +
                "AM_API_COMMENTS.COMMENT_TEXT, " +
                "AM_API_COMMENTS.CREATED_BY, " +
                "AM_API_COMMENTS.CREATED_TIME, " +
                "AM_API_COMMENTS.UPDATED_TIME, " +
                "AM_API_COMMENTS.API_ID, " +
                "AM_API_COMMENTS.PARENT_COMMENT_ID, " +
                "AM_API_COMMENTS.ENTRY_POINT, " +
                "AM_API_COMMENTS.CATEGORY " +
            "FROM " +
                "AM_API_COMMENTS, " +
                "AM_API API " +
            "WHERE " +
                "API.API_UUID = ? " +
                "AND API.API_ID = AM_API_COMMENTS.API_ID " +
                "AND PARENT_COMMENT_ID = ? " +
                "ORDER BY AM_API_COMMENTS.CREATED_TIME ASC LIMIT ? , ?";

    public static final String GET_ROOT_COMMENTS_SQL =
            "SELECT " +
                "AM_API_COMMENTS.COMMENT_ID, " +
                "AM_API_COMMENTS.COMMENT_TEXT, " +
                "AM_API_COMMENTS.CREATED_BY, " +
                "AM_API_COMMENTS.CREATED_TIME, " +
                "AM_API_COMMENTS.UPDATED_TIME, " +
                "AM_API_COMMENTS.API_ID, " +
                "AM_API_COMMENTS.PARENT_COMMENT_ID, " +
                "AM_API_COMMENTS.ENTRY_POINT, " +
                "AM_API_COMMENTS.CATEGORY " +
            "FROM " +
                "AM_API_COMMENTS, " +
                "AM_API API " +
            "WHERE " +
                "API.API_UUID = ? " +
                "AND API.API_ID = AM_API_COMMENTS.API_ID " +
                "AND PARENT_COMMENT_ID IS NULL " +
                "ORDER BY AM_API_COMMENTS.CREATED_TIME DESC LIMIT ? , ?";

}





