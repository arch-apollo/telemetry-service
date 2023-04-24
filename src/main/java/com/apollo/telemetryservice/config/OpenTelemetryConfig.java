package com.apollo.telemetryservice.config;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.jaeger.JaegerGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OpenTelemetryConfig implements WebMvcConfigurer {

    @Bean
    public Tracer tracer() {
        SdkTracerProvider tracerProvider = SdkTracerProvider.builder().build();

        SpanExporter jaegerExporter =
                JaegerGrpcSpanExporter.builder().setEndpoint(System.getenv("JAEGER_ENDPOINT")).build();

        tracerProvider.addSpanProcessor(SimpleSpanProcessor.builder(jaegerExporter).build());
        GlobalOpenTelemetry.set(OpenTelemetrySdk.builder().setTracerProvider(tracerProvider));
    }
}
