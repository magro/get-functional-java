/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.javakaffee.sandbox.getfj;

/**
 * Represents the profile of a person on some internet portal.
 * @author Martin Grotzke
 */
public class WebIdentity {

    private final String portalHomePageUrl;
    private final String profileUrl;
    public WebIdentity(final String portalHomePageUrl, final String profileUrl) {
        this.portalHomePageUrl = portalHomePageUrl;
        this.profileUrl = profileUrl;
    }
    public String getPortalHomePageUrl() {
        return portalHomePageUrl;
    }
    public String getProfileUrl() {
        return profileUrl;
    }
}
