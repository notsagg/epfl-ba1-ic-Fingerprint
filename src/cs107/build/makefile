JC     = javac
JFLAGS = -d . # -Xlint -Werror
SRC    = $(wildcard ../*.java)
OBJS   = $(notdir $(SRC:.java=.class))
TSRC   = $(wildcard ../tests/Test*.java) $(wildcard ../tests/*/Test*.java)
TOBJS  = $(notdir $(TSRC:.java=.class))
PACKAGE = cs107

.PHONY: all test
all: test
test: $(OBJS) $(TOBJS)

vpath %.java $(dir $(SRC))
%.class: %.java
	@echo "-- Building Java object $(PACKAGE)/$@"
	@$(JC) $(JFLAGS) $<

vpath Test%.java $(dir $(TSRC))
Test%.class: Test%.java
	@echo "-- Building Java test executable $@"
	@$(JC) $(JFLAGS) $<

.PHONY: clean clean-deps
clean:
	@find . -maxdepth 1 -type f \( -name "*.class" -o -name "*.png" \) -delete
clean-deps:
	@find deps/junit -maxdepth 1 -type d -name "junit*" -exec rm -r {} \;
