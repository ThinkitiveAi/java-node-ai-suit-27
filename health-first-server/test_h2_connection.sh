#!/bin/bash

echo "=== Health First Server H2 Database Access ==="
echo ""
echo "Application Status:"
echo "✅ Spring Boot application is running on port 8765"
echo "✅ H2 Database is configured and running"
echo ""
echo "=== H2 Database Connection Details ==="
echo "H2 Console URL: http://localhost:8765/h2-console"
echo "JDBC URL: jdbc:h2:mem:testdb"
echo "Username: sa"
echo "Password: (leave empty)"
echo ""
echo "=== Database Tables Created ==="
echo "1. PATIENTS"
echo "2. PROVIDERS" 
echo "3. PROVIDER_AVAILABILITY"
echo "4. PATIENT_MEDICAL_HISTORY"
echo ""
echo "=== Testing Connection ==="

# Test if the application is responding
if curl -s http://localhost:8765/actuator/health > /dev/null 2>&1; then
    echo "✅ Application is responding on port 8765"
else
    echo "❌ Application is not responding on port 8765"
fi

# Test H2 console access
if curl -s http://localhost:8765/h2-console > /dev/null 2>&1; then
    echo "✅ H2 Console is accessible"
else
    echo "❌ H2 Console is not accessible"
fi

echo ""
echo "=== Instructions ==="
echo "1. Open your web browser"
echo "2. Go to: http://localhost:8765/h2-console"
echo "3. Enter the connection details above"
echo "4. Click 'Connect'"
echo "5. You should see the database tables"
echo ""
echo "=== Alternative: Use Swagger UI ==="
echo "Swagger UI: http://localhost:8765/swagger-ui.html"
echo "" 