cmake_minimum_required(VERSION 3.22.1)

project("security_secrets")

add_library(
        security_secrets
        SHARED
        src/main/cpp/secrets.cpp
)

find_library(
        log-lib
        log
)

target_link_libraries(
        security_secrets
        ${log-lib}
)
