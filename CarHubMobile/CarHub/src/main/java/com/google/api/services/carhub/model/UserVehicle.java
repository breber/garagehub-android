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
 * on 2013-06-24 at 23:57:07 UTC 
 * Modify at your own risk.
 */

package com.google.api.services.carhub.model;

/**
 * Model definition for UserVehicle.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the . For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class UserVehicle extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String color;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String lastmodified;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String make;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String model;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String plates;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("server_id")
  private java.lang.String serverId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String year;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getColor() {
    return color;
  }

  /**
   * @param color color or {@code null} for none
   */
  public UserVehicle setColor(java.lang.String color) {
    this.color = color;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getLastmodified() {
    return lastmodified;
  }

  /**
   * @param lastmodified lastmodified or {@code null} for none
   */
  public UserVehicle setLastmodified(java.lang.String lastmodified) {
    this.lastmodified = lastmodified;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getMake() {
    return make;
  }

  /**
   * @param make make or {@code null} for none
   */
  public UserVehicle setMake(java.lang.String make) {
    this.make = make;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getModel() {
    return model;
  }

  /**
   * @param model model or {@code null} for none
   */
  public UserVehicle setModel(java.lang.String model) {
    this.model = model;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getPlates() {
    return plates;
  }

  /**
   * @param plates plates or {@code null} for none
   */
  public UserVehicle setPlates(java.lang.String plates) {
    this.plates = plates;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getServerId() {
    return serverId;
  }

  /**
   * @param serverId serverId or {@code null} for none
   */
  public UserVehicle setServerId(java.lang.String serverId) {
    this.serverId = serverId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getYear() {
    return year;
  }

  /**
   * @param year year or {@code null} for none
   */
  public UserVehicle setYear(java.lang.String year) {
    this.year = year;
    return this;
  }

  @Override
  public UserVehicle set(String fieldName, Object value) {
    return (UserVehicle) super.set(fieldName, value);
  }

  @Override
  public UserVehicle clone() {
    return (UserVehicle) super.clone();
  }

}
