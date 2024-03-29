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
 * This code was generated by https://github.com/google/apis-client-generator/
 * (build: 2017-11-07 19:12:12 UTC)
 * on 2017-11-25 at 02:46:02 UTC 
 * Modify at your own risk.
 */

package com.appspot.car_hub.garagehub.model;

/**
 * Model definition for MaintenanceRecord.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the garagehub. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MaintenanceRecord extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Double amount;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long categoryid;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String date;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String description;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String lastmodified;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String location;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer odometer;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String pictureurl;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("server_id")
  private java.lang.String serverId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long vehicle;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getAmount() {
    return amount;
  }

  /**
   * @param amount amount or {@code null} for none
   */
  public MaintenanceRecord setAmount(java.lang.Double amount) {
    this.amount = amount;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getCategoryid() {
    return categoryid;
  }

  /**
   * @param categoryid categoryid or {@code null} for none
   */
  public MaintenanceRecord setCategoryid(java.lang.Long categoryid) {
    this.categoryid = categoryid;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDate() {
    return date;
  }

  /**
   * @param date date or {@code null} for none
   */
  public MaintenanceRecord setDate(java.lang.String date) {
    this.date = date;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDescription() {
    return description;
  }

  /**
   * @param description description or {@code null} for none
   */
  public MaintenanceRecord setDescription(java.lang.String description) {
    this.description = description;
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
  public MaintenanceRecord setLastmodified(java.lang.String lastmodified) {
    this.lastmodified = lastmodified;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getLocation() {
    return location;
  }

  /**
   * @param location location or {@code null} for none
   */
  public MaintenanceRecord setLocation(java.lang.String location) {
    this.location = location;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getOdometer() {
    return odometer;
  }

  /**
   * @param odometer odometer or {@code null} for none
   */
  public MaintenanceRecord setOdometer(java.lang.Integer odometer) {
    this.odometer = odometer;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getPictureurl() {
    return pictureurl;
  }

  /**
   * @param pictureurl pictureurl or {@code null} for none
   */
  public MaintenanceRecord setPictureurl(java.lang.String pictureurl) {
    this.pictureurl = pictureurl;
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
  public MaintenanceRecord setServerId(java.lang.String serverId) {
    this.serverId = serverId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getVehicle() {
    return vehicle;
  }

  /**
   * @param vehicle vehicle or {@code null} for none
   */
  public MaintenanceRecord setVehicle(java.lang.Long vehicle) {
    this.vehicle = vehicle;
    return this;
  }

  @Override
  public MaintenanceRecord set(String fieldName, Object value) {
    return (MaintenanceRecord) super.set(fieldName, value);
  }

  @Override
  public MaintenanceRecord clone() {
    return (MaintenanceRecord) super.clone();
  }

}
