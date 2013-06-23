/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2013-06-20 19:08:55 UTC)
 * on 2013-06-23 at 21:25:57 UTC 
 * Modify at your own risk.
 */

package com.google.api.services.carhub;

/**
 * Service definition for Carhub (v1).
 *
 * <p>
 * CarHub API
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link CarhubRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class Carhub extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.15.0-rc of the  library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://car-hub.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "carhub/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public Carhub(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  Carhub(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * An accessor for creating requests from the Expense collection.
   *
   * <p>The typical use is:</p>
   * <pre>
   *   {@code Carhub carhub = new Carhub(...);}
   *   {@code Carhub.Expense.List request = carhub.expense().list(parameters ...)}
   * </pre>
   *
   * @return the resource collection
   */
  public Expense expense() {
    return new Expense();
  }

  /**
   * The "expense" collection of methods.
   */
  public class Expense {

    /**
     * Create a request for the method "expense.list".
     *
     * This request holds the parameters needed by the the carhub server.  After setting any optional
     * parameters, call the {@link List#execute()} method to invoke the remote operation.
     *
     * @param vehicle
     * @return the request
     */
    public List list(java.lang.Integer vehicle) throws java.io.IOException {
      List result = new List(vehicle);
      initialize(result);
      return result;
    }

    public class List extends CarhubRequest<com.google.api.services.carhub.model.BaseExpenseCollection> {

      private static final String REST_PATH = "expense/list/{vehicle}";

      /**
       * Create a request for the method "expense.list".
       *
       * This request holds the parameters needed by the the carhub server.  After setting any optional
       * parameters, call the {@link List#execute()} method to invoke the remote operation. <p> {@link
       * List#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)} must be
       * called to initialize this instance immediately after invoking the constructor. </p>
       *
       * @param vehicle
       * @since 1.13
       */
      protected List(java.lang.Integer vehicle) {
        super(Carhub.this, "GET", REST_PATH, null, com.google.api.services.carhub.model.BaseExpenseCollection.class);
        this.vehicle = com.google.api.client.util.Preconditions.checkNotNull(vehicle, "Required parameter vehicle must be specified.");
      }

      @Override
      public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
        return super.executeUsingHead();
      }

      @Override
      public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
        return super.buildHttpRequestUsingHead();
      }

      @Override
      public List setAlt(java.lang.String alt) {
        return (List) super.setAlt(alt);
      }

      @Override
      public List setFields(java.lang.String fields) {
        return (List) super.setFields(fields);
      }

      @Override
      public List setKey(java.lang.String key) {
        return (List) super.setKey(key);
      }

      @Override
      public List setOauthToken(java.lang.String oauthToken) {
        return (List) super.setOauthToken(oauthToken);
      }

      @Override
      public List setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (List) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public List setQuotaUser(java.lang.String quotaUser) {
        return (List) super.setQuotaUser(quotaUser);
      }

      @Override
      public List setUserIp(java.lang.String userIp) {
        return (List) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private java.lang.Integer vehicle;

      /**

       */
      public java.lang.Integer getVehicle() {
        return vehicle;
      }

      public List setVehicle(java.lang.Integer vehicle) {
        this.vehicle = vehicle;
        return this;
      }

      @Override
      public List set(String parameterName, Object value) {
        return (List) super.set(parameterName, value);
      }
    }

  }

  /**
   * An accessor for creating requests from the Maintenance collection.
   *
   * <p>The typical use is:</p>
   * <pre>
   *   {@code Carhub carhub = new Carhub(...);}
   *   {@code Carhub.Maintenance.List request = carhub.maintenance().list(parameters ...)}
   * </pre>
   *
   * @return the resource collection
   */
  public Maintenance maintenance() {
    return new Maintenance();
  }

  /**
   * The "maintenance" collection of methods.
   */
  public class Maintenance {

    /**
     * Create a request for the method "maintenance.list".
     *
     * This request holds the parameters needed by the the carhub server.  After setting any optional
     * parameters, call the {@link List#execute()} method to invoke the remote operation.
     *
     * @param vehicle
     * @return the request
     */
    public List list(java.lang.Integer vehicle) throws java.io.IOException {
      List result = new List(vehicle);
      initialize(result);
      return result;
    }

    public class List extends CarhubRequest<com.google.api.services.carhub.model.MaintenanceRecordCollection> {

      private static final String REST_PATH = "maintenance/list/{vehicle}";

      /**
       * Create a request for the method "maintenance.list".
       *
       * This request holds the parameters needed by the the carhub server.  After setting any optional
       * parameters, call the {@link List#execute()} method to invoke the remote operation. <p> {@link
       * List#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)} must be
       * called to initialize this instance immediately after invoking the constructor. </p>
       *
       * @param vehicle
       * @since 1.13
       */
      protected List(java.lang.Integer vehicle) {
        super(Carhub.this, "GET", REST_PATH, null, com.google.api.services.carhub.model.MaintenanceRecordCollection.class);
        this.vehicle = com.google.api.client.util.Preconditions.checkNotNull(vehicle, "Required parameter vehicle must be specified.");
      }

      @Override
      public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
        return super.executeUsingHead();
      }

      @Override
      public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
        return super.buildHttpRequestUsingHead();
      }

      @Override
      public List setAlt(java.lang.String alt) {
        return (List) super.setAlt(alt);
      }

      @Override
      public List setFields(java.lang.String fields) {
        return (List) super.setFields(fields);
      }

      @Override
      public List setKey(java.lang.String key) {
        return (List) super.setKey(key);
      }

      @Override
      public List setOauthToken(java.lang.String oauthToken) {
        return (List) super.setOauthToken(oauthToken);
      }

      @Override
      public List setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (List) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public List setQuotaUser(java.lang.String quotaUser) {
        return (List) super.setQuotaUser(quotaUser);
      }

      @Override
      public List setUserIp(java.lang.String userIp) {
        return (List) super.setUserIp(userIp);
      }

      @com.google.api.client.util.Key
      private java.lang.Integer vehicle;

      /**

       */
      public java.lang.Integer getVehicle() {
        return vehicle;
      }

      public List setVehicle(java.lang.Integer vehicle) {
        this.vehicle = vehicle;
        return this;
      }

      @Override
      public List set(String parameterName, Object value) {
        return (List) super.set(parameterName, value);
      }
    }

  }

  /**
   * An accessor for creating requests from the Vehicle collection.
   *
   * <p>The typical use is:</p>
   * <pre>
   *   {@code Carhub carhub = new Carhub(...);}
   *   {@code Carhub.Vehicle.List request = carhub.vehicle().list(parameters ...)}
   * </pre>
   *
   * @return the resource collection
   */
  public Vehicle vehicle() {
    return new Vehicle();
  }

  /**
   * The "vehicle" collection of methods.
   */
  public class Vehicle {

    /**
     * Create a request for the method "vehicle.delete".
     *
     * This request holds the parameters needed by the the carhub server.  After setting any optional
     * parameters, call the {@link Delete#execute()} method to invoke the remote operation.
     *
     * @param content the {@link com.google.api.services.carhub.model.UserVehicle}
     * @return the request
     */
    public Delete delete(com.google.api.services.carhub.model.UserVehicle content) throws java.io.IOException {
      Delete result = new Delete(content);
      initialize(result);
      return result;
    }

    public class Delete extends CarhubRequest<com.google.api.services.carhub.model.UserVehicle> {

      private static final String REST_PATH = "vehicle/delete";

      /**
       * Create a request for the method "vehicle.delete".
       *
       * This request holds the parameters needed by the the carhub server.  After setting any optional
       * parameters, call the {@link Delete#execute()} method to invoke the remote operation. <p> {@link
       * Delete#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)} must
       * be called to initialize this instance immediately after invoking the constructor. </p>
       *
       * @param content the {@link com.google.api.services.carhub.model.UserVehicle}
       * @since 1.13
       */
      protected Delete(com.google.api.services.carhub.model.UserVehicle content) {
        super(Carhub.this, "POST", REST_PATH, content, com.google.api.services.carhub.model.UserVehicle.class);
      }

      @Override
      public Delete setAlt(java.lang.String alt) {
        return (Delete) super.setAlt(alt);
      }

      @Override
      public Delete setFields(java.lang.String fields) {
        return (Delete) super.setFields(fields);
      }

      @Override
      public Delete setKey(java.lang.String key) {
        return (Delete) super.setKey(key);
      }

      @Override
      public Delete setOauthToken(java.lang.String oauthToken) {
        return (Delete) super.setOauthToken(oauthToken);
      }

      @Override
      public Delete setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (Delete) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public Delete setQuotaUser(java.lang.String quotaUser) {
        return (Delete) super.setQuotaUser(quotaUser);
      }

      @Override
      public Delete setUserIp(java.lang.String userIp) {
        return (Delete) super.setUserIp(userIp);
      }

      @Override
      public Delete set(String parameterName, Object value) {
        return (Delete) super.set(parameterName, value);
      }
    }
    /**
     * Create a request for the method "vehicle.list".
     *
     * This request holds the parameters needed by the the carhub server.  After setting any optional
     * parameters, call the {@link List#execute()} method to invoke the remote operation.
     *
     * @return the request
     */
    public List list() throws java.io.IOException {
      List result = new List();
      initialize(result);
      return result;
    }

    public class List extends CarhubRequest<com.google.api.services.carhub.model.UserVehicleCollection> {

      private static final String REST_PATH = "vehicle/list";

      /**
       * Create a request for the method "vehicle.list".
       *
       * This request holds the parameters needed by the the carhub server.  After setting any optional
       * parameters, call the {@link List#execute()} method to invoke the remote operation. <p> {@link
       * List#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)} must be
       * called to initialize this instance immediately after invoking the constructor. </p>
       *
       * @since 1.13
       */
      protected List() {
        super(Carhub.this, "GET", REST_PATH, null, com.google.api.services.carhub.model.UserVehicleCollection.class);
      }

      @Override
      public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
        return super.executeUsingHead();
      }

      @Override
      public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
        return super.buildHttpRequestUsingHead();
      }

      @Override
      public List setAlt(java.lang.String alt) {
        return (List) super.setAlt(alt);
      }

      @Override
      public List setFields(java.lang.String fields) {
        return (List) super.setFields(fields);
      }

      @Override
      public List setKey(java.lang.String key) {
        return (List) super.setKey(key);
      }

      @Override
      public List setOauthToken(java.lang.String oauthToken) {
        return (List) super.setOauthToken(oauthToken);
      }

      @Override
      public List setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (List) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public List setQuotaUser(java.lang.String quotaUser) {
        return (List) super.setQuotaUser(quotaUser);
      }

      @Override
      public List setUserIp(java.lang.String userIp) {
        return (List) super.setUserIp(userIp);
      }

      @Override
      public List set(String parameterName, Object value) {
        return (List) super.set(parameterName, value);
      }
    }
    /**
     * Create a request for the method "vehicle.store".
     *
     * This request holds the parameters needed by the the carhub server.  After setting any optional
     * parameters, call the {@link Store#execute()} method to invoke the remote operation.
     *
     * @param content the {@link com.google.api.services.carhub.model.UserVehicle}
     * @return the request
     */
    public Store store(com.google.api.services.carhub.model.UserVehicle content) throws java.io.IOException {
      Store result = new Store(content);
      initialize(result);
      return result;
    }

    public class Store extends CarhubRequest<com.google.api.services.carhub.model.UserVehicle> {

      private static final String REST_PATH = "vehicle/store";

      /**
       * Create a request for the method "vehicle.store".
       *
       * This request holds the parameters needed by the the carhub server.  After setting any optional
       * parameters, call the {@link Store#execute()} method to invoke the remote operation. <p> {@link
       * Store#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)} must
       * be called to initialize this instance immediately after invoking the constructor. </p>
       *
       * @param content the {@link com.google.api.services.carhub.model.UserVehicle}
       * @since 1.13
       */
      protected Store(com.google.api.services.carhub.model.UserVehicle content) {
        super(Carhub.this, "POST", REST_PATH, content, com.google.api.services.carhub.model.UserVehicle.class);
      }

      @Override
      public Store setAlt(java.lang.String alt) {
        return (Store) super.setAlt(alt);
      }

      @Override
      public Store setFields(java.lang.String fields) {
        return (Store) super.setFields(fields);
      }

      @Override
      public Store setKey(java.lang.String key) {
        return (Store) super.setKey(key);
      }

      @Override
      public Store setOauthToken(java.lang.String oauthToken) {
        return (Store) super.setOauthToken(oauthToken);
      }

      @Override
      public Store setPrettyPrint(java.lang.Boolean prettyPrint) {
        return (Store) super.setPrettyPrint(prettyPrint);
      }

      @Override
      public Store setQuotaUser(java.lang.String quotaUser) {
        return (Store) super.setQuotaUser(quotaUser);
      }

      @Override
      public Store setUserIp(java.lang.String userIp) {
        return (Store) super.setUserIp(userIp);
      }

      @Override
      public Store set(String parameterName, Object value) {
        return (Store) super.set(parameterName, value);
      }
    }

  }

  /**
   * Builder for {@link Carhub}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link Carhub}. */
    @Override
    public Carhub build() {
      return new Carhub(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link CarhubRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setCarhubRequestInitializer(
        CarhubRequestInitializer carhubRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(carhubRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
