package com.dilip.countrycodepickerexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dilip.country_code_picker.CCPTransformer
import com.dilip.country_code_picker.CCPUtils
import com.dilip.country_code_picker.CCPValidator
import com.dilip.country_code_picker.Country
import com.dilip.country_code_picker.CountryCodePicker
import com.dilip.country_code_picker.CountryCodePickerTextField
import com.dilip.country_code_picker.PickerCustomization
import com.dilip.country_code_picker.ViewCustomization
import com.dilip.countrycodepickerexample.ui.theme.CountryCodePickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CountryCodePickerTheme {
                CountryCodePickerPreview()
            }
        }
    }
}

@Composable
fun ShowMainScreen() {
    Column(modifier = Modifier.fillMaxSize()) {

        BasicCountryCodePicker()

        Spacer(modifier = Modifier.padding(20.dp))


        ShowCCPWithTextField()

        Spacer(modifier = Modifier.padding(20.dp))


        ShowCountryCodePickerTextField()

    }
}

@Composable
fun BasicCountryCodePicker() {

    Column(Modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Country Code Picker",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        )

        Text(
            text = "Full Screen",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(10.dp, 0.dp)
                .fillMaxWidth()
        )

        var country by remember {
            mutableStateOf(Country.India)
        }

        if (!LocalInspectionMode.current) {
            CCPUtils.getCountryAutomatically(context = LocalContext.current).let {
                it?.let {
                    country = it
                }
            }
        }

        CountryCodePicker(
            modifier = Modifier.fillMaxWidth(1f),
            selectedCountry = country,
            onCountrySelected = { country = it },
            viewCustomization = ViewCustomization(
                showFlag = true,
                showCountryIso = true,
                showCountryName = true,
                showCountryCode = true,
                clipToFull = true
            ),
            pickerCustomization = PickerCustomization(
                showFlag = false,
            ),
            showSheet = true,
        )

        Spacer(modifier = Modifier.padding(10.dp))


        Text(
            text = "Small Size",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(10.dp, 0.dp)
                .fillMaxWidth()
        )

        CountryCodePicker(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            selectedCountry = country,
            onCountrySelected = { country = it },
            viewCustomization = ViewCustomization(
                showFlag = true,
                showCountryIso = false,
                showCountryName = false,
                showCountryCode = true,
                clipToFull = false
            ),
            pickerCustomization = PickerCustomization(
                showFlag = false,
            ),
            showSheet = true,
        )

    }


}

@Composable
fun ShowCountryCodePickerTextField() {

    Text(
        text = "Integrated TextField",
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    )


    var text by remember { mutableStateOf("") }

    var country by remember {
        mutableStateOf(Country.India)
    }

    if (!LocalInspectionMode.current) {
        CCPUtils.getCountryAutomatically(context = LocalContext.current).let {
            it?.let {
                country = it
            }
        }
    }


    CountryCodePickerTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        enabled = true,
        textStyle = MaterialTheme.typography.bodyMedium,
        trailingIcon = {
            IconButton(onClick = { text = "" }) {
                Icon(
                    imageVector = Icons.Default.Clear, contentDescription = "Clear"
                )
            }
        },
        label = {
            Text(
                text = "Phone Number", style = MaterialTheme.typography.bodyMedium
            )
        },
        showError = true,
        shape = RoundedCornerShape(10.dp),
        onValueChange = { _, value, _ ->
            text = value
        },
        number = text,
        showSheet = true,
        selectedCountry = country


    )

}

@Composable
fun ShowCCPWithTextField() {

    Text(
        text = "Attached to TextField",
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    )

    val context = LocalContext.current

    var text by remember { mutableStateOf("") }

    var country by remember {
        mutableStateOf(Country.UnitedStates)
    }

    val validatePhoneNumber = remember(context) {
        CCPValidator(context = context)
    }

    var isNumberValid: Boolean by rememberSaveable(country, text) {
        mutableStateOf(
            validatePhoneNumber(
                number = text, countryCode = country.countryCode
            ),
        )
    }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            isNumberValid = validatePhoneNumber(
                number = it, countryCode = country.countryCode
            )

        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 5.dp),
        textStyle = MaterialTheme.typography.bodyMedium,
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        placeholder = {
            Text(
                text = "Enter Your Phone Number", style = MaterialTheme.typography.bodyMedium
            )
        },
        label = {
            Text(
                text = "Phone Number", style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = {
            CountryCodePicker(
                selectedCountry = country,
                countryList = Country.getAllCountries(),
                onCountrySelected = {
                    country = it
                    isNumberValid = validatePhoneNumber(
                        number = text, countryCode = it.countryCode
                    )
                },

                )

        },
        trailingIcon = {
            IconButton(onClick = {

            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "drop down icon"
                )
            }
        },
        isError = !isNumberValid && text.isNotEmpty(),
        visualTransformation = CCPTransformer(context, country.countryIso),

        )

}

@Preview(showBackground = true)
@Composable
fun CountryCodePickerPreview() {
    CountryCodePickerTheme {
        ShowMainScreen()
    }
}