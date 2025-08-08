package com.dilip.country_code_picker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CountryCodePickerTextField(
    modifier: Modifier = Modifier,
    number: String,
    onValueChange: (countryCode: String, value: String, isValid: Boolean) -> Unit,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    showError: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    shape: Shape = RoundedCornerShape(10.dp),
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    selectedCountry: Country = Country.India,
    countryList: List<Country> = Country.getAllCountries(),
    viewCustomization: ViewCustomization = ViewCustomization(),
    pickerCustomization: PickerCustomization = PickerCustomization(),
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    showSheet: Boolean = false,
    itemPadding: Int = 10,
) {
    val context = LocalContext.current

    var country by remember { mutableStateOf(selectedCountry) }
    val validatePhoneNumber = remember(context) { CCPValidator(context) }
    var isNumberValid by rememberSaveable(country, number) {
        mutableStateOf(validatePhoneNumber(number, country.countryCode))
    }

    OutlinedTextField(
        value = number,
        onValueChange = {
            isNumberValid = validatePhoneNumber(it, country.countryCode)
            onValueChange(country.countryCode, it, isNumberValid)
        },
        modifier = modifier.fillMaxWidth(),
        textStyle = textStyle,
        singleLine = true,
        shape = shape,
        label = label,
        placeholder = placeholder,
        leadingIcon = {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .clickable(
                        enabled = enabled,
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onClick()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                CountryCodePicker(
                    selectedCountry = country,
                    countryList = countryList,
                    onCountrySelected = {
                        country = it
                        isNumberValid = validatePhoneNumber(number, it.countryCode)
                        onValueChange(it.countryCode, number, isNumberValid)
                    },
                    viewCustomization = viewCustomization,
                    pickerCustomization = pickerCustomization,
                    backgroundColor = backgroundColor,
                    textStyle = textStyle,
                    showSheet = showSheet,
                    itemPadding = itemPadding
                )
            }
        },
        trailingIcon = trailingIcon,
        isError = !isNumberValid && number.isNotEmpty() && showError,
        visualTransformation = CCPTransformer(context, country.countryIso),
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        colors = colors
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

    var value by remember {
        mutableStateOf("")
    }

    CountryCodePickerTextField(onValueChange = { _, number, _ ->
        value = number

    }, number = value)
}