package org.mindtickle.utils;

import java.io.IOException;

@FunctionalInterface
public interface IStub<T> {
void createStubMapping() throws IOException;
}
