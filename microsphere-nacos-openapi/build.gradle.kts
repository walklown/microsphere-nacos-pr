/*
 * This file was generated by the Gradle 'init' task.
 *
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    id("buildlogic.kotlin-library-conventions")
}

dependencies {

    // Apache Http Client
    api(libs.apache.http.client)

    // Gson
    api(libs.gson)

    // Testing
    testImplementation(libs.junit.jupiter.engine)

}
