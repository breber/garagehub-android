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
 * (build: 2013-06-26 16:27:34 UTC)
 * on 2013-07-02 at 23:39:11 UTC 
 * Modify at your own risk.
 */

package com.google.api.services.carhub.model;

/**
 * Model definition for UserExpenseRecord.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the . For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class UserExpenseRecord extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Double amount;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer categoryid;

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
  private java.lang.String pictureurl;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key("server_id")
  private java.lang.String serverId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer vehicle;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Double getAmount() {
    return amount;
  }

  /**
   * @param amount amount or {@code null} for none
   */
  public UserExpenseRecord setAmount(java.lang.Double amount) {
    this.amount = amount;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getCategoryid() {
    return categoryid;
  }

  /**
   * @param categoryid categoryid or {@code null} for none
   */
  public UserExpenseRecord setCategoryid(java.lang.Integer categoryid) {
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
  public UserExpenseRecord setDate(java.lang.String date) {
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
  public UserExpenseRecord setDescription(java.lang.String description) {
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
  public UserExpenseRecord setLastmodified(java.lang.String lastmodified) {
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
  public UserExpenseRecord setLocation(java.lang.String location) {
    this.location = location;
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
  public UserExpenseRecord setPictureurl(java.lang.String pictureurl) {
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
  public UserExpenseRecord setServerId(java.lang.String serverId) {
    this.serverId = serverId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getVehicle() {
    return vehicle;
  }

  /**
   * @param vehicle vehicle or {@code null} for none
   */
  public UserExpenseRecord setVehicle(java.lang.Integer vehicle) {
    this.vehicle = vehicle;
    return this;
  }

  @Override
  public UserExpenseRecord set(String fieldName, Object value) {
    return (UserExpenseRecord) super.set(fieldName, value);
  }

  @Override
  public UserExpenseRecord clone() {
    return (UserExpenseRecord) super.clone();
  }

}
