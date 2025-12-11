FROM ghcr.io/eclipse-ee4j/glassfish:7.0.23@sha256:4c9941c99201a489992ce88196250ea8056b3bd998b589b822e05dd8fb759eae

COPY postgresql-42.7.4.jar /opt/glassfish7/glassfish/domains/domain1/lib/


# Copy admin script
COPY setup.asadmin /opt/glassfish7/setup.asadmin


# Configure domain: start, run asadmin, stop (image build-time)
RUN asadmin start-domain && \
    asadmin --interactive=false < /opt/glassfish7/setup.asadmin && \
    asadmin stop-domain

# Standard GlassFish entrypoint
CMD ["asadmin", "start-domain", "-v"]