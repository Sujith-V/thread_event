package io.sujithworkshop.threadevent.core;

public interface EventLifeCycle
{
	void start() throws Exception;

	void prePersist() throws Exception;

	void postPersist() throws Exception;

	void success() throws Exception;

	void fail() throws Exception;

	void end() throws Exception;
}