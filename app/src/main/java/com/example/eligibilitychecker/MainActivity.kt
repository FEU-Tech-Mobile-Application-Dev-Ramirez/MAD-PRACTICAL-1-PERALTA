package com.example.eligibilitychecker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eligibilitychecker.ui.theme.EligibilityCheckerTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EligibilityCheckerTheme {
                EligibilityChecker()
            }
        }
    }
}

@Composable
fun EligibilityChecker(){
    val name = remember { mutableStateOf("") }
    val dob = remember { mutableStateOf("") }
    val result = remember { mutableStateOf("") }

    Column (modifier = Modifier.padding(16.dp)){
        Text("Eligibility Checker", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = name.value,
            onValueChange = { name.value = it},
            label = { Text("Enter your name: ") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = dob.value,
            onValueChange = {dob.value = it},
            label = { Text("Enter your Date of Birth: (MM/DD/YYYY) ")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            result.value = checkEligibility(dob.value, name.value)
        }){
            Text("Check Eligibility to drink alcohol in Philippines")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = result.value)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            result.value =""
            dob.value =""
            name.value =""
        }){
            Text("RESET")
        }
    }
}

fun checkEligibility(dob: String, name: String): String {
    if (dob.isEmpty() || name.isEmpty()){
        return "Please fill in the blank."
    }

    else
    try {
        val age = calculateAge(dob)
        return if (age >= 18)
            "Congratulations $name! You are eligible to drink alcohol."
        else
            "Sorry $name, you are under-aged! It is illegal to drink alcohol."
    }
    catch(e: IllegalArgumentException){
        return e.message + " Please try again."
    }
    catch (e: Exception){
        return "Invalid date format. Use MM/DD/YYYY"
    }
}

fun calculateAge(dob: String): Int{
    val parts = dob.split("/")
    if (parts.size !=3) throw IllegalArgumentException("Invalid date format")

    val birthMonth = parts[0].toInt()
    val birthDay = parts[1].toInt()
    val birthYear = parts[2].toInt()

    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)+1
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    if (birthMonth !in 1..12) throw IllegalArgumentException("Invalid month: $birthMonth. Must be between 1 and 12.")
    if (birthDay !in 1..31) throw IllegalArgumentException("Invalid day: $birthDay. Must be between 1 and 31.")
    if (birthYear > currentYear || birthYear < 1900) {
        throw IllegalArgumentException("Invalid year: $birthYear. Must be between 1900 and $currentYear.")
    }

    var age = currentYear - birthYear
    if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)){
        age--
    }
    return age
}