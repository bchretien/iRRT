CMAKE_MINIMUM_REQUIRED(VERSION 2.8)
ENABLE_LANGUAGE(CXX)
SET(CMAKE_VERBOSE_MAKEFILE OFF)

PROJECT(iRRT)

SET(CMAKE_MODULE_PATH ${CMAKE_MODULE_PATH} "${CMAKE_SOURCE_DIR}/CMakeModules/")

# Java required
FIND_PACKAGE(Java REQUIRED COMPONENTS Development)

# Basic macros for the generation of jar files
INCLUDE(${CMAKE_SOURCE_DIR}/CMakeModules/UseJava.cmake)

# Include variable JAVA_ALIENFX_SRC
INCLUDE(src/SourcesLib.cmake)

SET(LIB_DEPENDENCIES
  ${CMAKE_SOURCE_DIR}/lib/appframework-1.0.3.jar
  ${CMAKE_SOURCE_DIR}/lib/swing-layout-1.0.4.jar
  ${CMAKE_SOURCE_DIR}/lib/swing-worker-1.1.jar
)

# Create jar
SET(CMAKE_JAVA_TARGET_OUTPUT_NAME iRRT)
SET(CMAKE_JAVA_JAR_ENTRY_POINT irrt/IRRTApp)
SET(CMAKE_JAVA_INCLUDE_PATH ${LIB_DEPENDENCIES})
ADD_JAR(iRRT ${JAVA_IRRT_SRC})

# Install in the root folder
INSTALL_JAR(iRRT ${CMAKE_CURRENT_SOURCE_DIR})

# Add the images to the executable jar
ADD_CUSTOM_TARGET(Images_to_jar ALL)
ADD_CUSTOM_COMMAND(
  COMMAND ${Java_JAR_EXECUTABLE}
  ARGS    uf ${CMAKE_BINARY_DIR}/iRRT.jar
          -C ${CMAKE_SOURCE_DIR}/src irrt/resources
  TARGET  Images_to_jar
)
ADD_DEPENDENCIES(Images_to_jar iRRT)

