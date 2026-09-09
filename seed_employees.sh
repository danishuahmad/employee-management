#!/bin/bash
# Seed script - creates 20 sample employees via the Employee Management API
# Usage: ./seed_employees.sh
# Make sure your Spring Boot app is running on localhost:8080 first!

BASE_URL="http://localhost:8080/employees"

create_employee() {
  local name="$1"
  local department="$2"
  local salary="$3"

  echo "Creating: $name ($department, \$$salary)..."
  curl -s -X POST "$BASE_URL" \
    -H "Content-Type: application/json" \
    -d "{\"name\":\"$name\",\"department\":\"$department\",\"salary\":$salary}" \
    -w "\n  -> HTTP %{http_code}\n"
}

# Engineering
create_employee "Anna Schmidt" "Engineering" 75000
create_employee "Ben Fischer" "Engineering" 68000
create_employee "Felix Wagner" "Engineering" 82000
create_employee "Greta Hoffmann" "Engineering" 71000
create_employee "Hans Zimmer" "Engineering" 90000

# Marketing
create_employee "Clara Weber" "Marketing" 55000
create_employee "David Becker" "Marketing" 60000
create_employee "Ines Schulz" "Marketing" 58000
create_employee "Jonas Krueger" "Marketing" 62000

# Sales
create_employee "Elena Braun" "Sales" 50000
create_employee "Karl Neumann" "Sales" 53000
create_employee "Lena Schaefer" "Sales" 56000
create_employee "Max Vogel" "Sales" 51000
create_employee "Nina Richter" "Sales" 59000

# HR
create_employee "Otto Klein" "HR" 48000
create_employee "Paula Wolf" "HR" 52000
create_employee "Quentin Schroeder" "HR" 49000

# Finance
create_employee "Rosa Bauer" "Finance" 72000
create_employee "Stefan Lange" "Finance" 78000
create_employee "Tina Krause" "Finance" 69000

echo ""
echo "Done. Fetching all employees to confirm..."
curl -s "$BASE_URL" | python3 -m json.tool