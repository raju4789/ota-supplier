FROM openjdk:11
ADD target/supplier-0.0.1-SNAPSHOT.jar supplier-0.0.1-SNAPSHOT.jar
EXPOSE 8082
ENTRYPOINT ["exec","java", "-jar", "supplier-0.0.1-SNAPSHOT.jar", "com.ogado.supplier.SupplierApplication.java"]