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
 * (build: 2013-11-22 19:59:01 UTC)
 * on 2013-11-25 at 01:39:38 UTC 
 * Modify at your own risk.
 */

package com.appspot.car_hub.carhub.model;

/**
 * Model definition for UserExpenseRecordCollection.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the carhub. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class UserExpenseRecordCollection extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<UserExpenseRecord> items;

  static {
    // hack to force ProGuard to consider UserExpenseRecord used, since otherwise it would be stripped out
    // see http://code.google.com/p/google-api-java-client/issues/detail?id=528
    com.google.api.client.util.Data.nullOf(UserExpenseRecord.class);
  }

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String nextPageToken;

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<UserExpenseRecord> getItems() {
    return items;
  }

  /**
   * @param items items or {@code null} for none
   */
  public UserExpenseRecordCollection setItems(java.util.List<UserExpenseRecord> items) {
    this.items = items;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getNextPageToken() {
    return nextPageToken;
  }

  /**
   * @param nextPageToken nextPageToken or {@code null} for none
   */
  public UserExpenseRecordCollection setNextPageToken(java.lang.String nextPageToken) {
    this.nextPageToken = nextPageToken;
    return this;
  }

  @Override
  public UserExpenseRecordCollection set(String fieldName, Object value) {
    return (UserExpenseRecordCollection) super.set(fieldName, value);
  }

  @Override
  public UserExpenseRecordCollection clone() {
    return (UserExpenseRecordCollection) super.clone();
  }

}
