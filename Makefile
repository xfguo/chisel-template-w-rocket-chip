base_dir := $(patsubst %/,%,$(dir $(abspath $(lastword $(MAKEFILE_LIST)))))
BUILD_DIR := $(base_dir)/builds
PROJECT := template

rocketchip_dir := $(base_dir)/lib/rocket-chip

SBT ?= java -jar $(rocketchip_dir)/sbt-launch.jar ++2.12.4

# Build firrtl.jar and put it where chisel3 can find it.
FIRRTL_JAR ?= $(rocketchip_dir)/firrtl/utils/bin/firrtl.jar
FIRRTL ?= java -Xmx2G -Xss8M -XX:MaxPermSize=256M -cp $(FIRRTL_JAR) firrtl.Driver

$(FIRRTL_JAR): $(shell find $(rocketchip_dir)/firrtl/src/main/scala -iname "*.scala")
	$(MAKE) -C $(rocketchip_dir)/firrtl SBT="$(SBT)" root_dir=$(rocketchip_dir)/firrtl build-scala
	touch $(FIRRTL_JAR)
	mkdir -p $(rocketchip_dir)/lib
	cp -p $(FIRRTL_JAR) $(rocketchip_dir)/lib
	mkdir -p $(rocketchip_dir)/chisel3/lib
	cp -p $(FIRRTL_JAR) $(rocketchip_dir)/chisel3/lib

# Build verilog
.PHONY: verilog
verilog: $(FIRRTL_JAR)
	$(SBT) "runMain example.Generator -td $(BUILD_DIR)"

# Clean
.PHONY: clean
clean:
	rm -rf $(BUILD_DIR)
