# Country-Code-Picker Library Documentation

The **Country-Code-Picker** is an Android library designed to provide a seamless way to integrate country code selection into mobile applications. It enables developers to add an intuitive interface where users can select their country code when entering phone numbers or selecting country-related settings. The library supports both a picker view and an integrated text field for phone number input.

## Features

- **Country Selection Picker**: Allows users to select a country code and displays relevant country details such as name, flag, and country code.
- **Integrated Text Field**: Adds a country code picker alongside a phone number text field for user input.
- **Customizable UI**: Customize the appearance of the country code picker and text field to fit your app’s design.
- **Phone Number Validation**: Ensures phone numbers are valid based on the selected country.
- **Edge-to-Edge Support**: Compatible with modern Android devices that use edge-to-edge screens.

## Installation

To add the **Country-Code-Picker** library to your Android project, you can use **JitPack**.

1. First, add the **JitPack** repository to your project’s `build.gradle` file:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. Then, add the following dependency in your `build.gradle` file:

```gradle
implementation("com.github.dilip2882:Country-Code-Picker-Compose-Library:1.0.0")
```

## Usage

### Basic Country Code Picker

The `CountryCodePicker` widget displays a country selection UI where users can pick a country, and it automatically updates the UI based on the selected country.

```kotlin
@Composable
fun BasicCountryCodePicker() {
    var country by remember { mutableStateOf(Country.India) }
    
    // Automatically fetch country based on device locale
    if (!LocalInspectionMode.current) {
        CCPUtils.getCountryAutomatically(context = LocalContext.current)?.let {
            country = it
        }
    }

    CountryCodePicker(
        selectedCountry = country,
        onCountrySelected = { country = it },
        viewCustomization = ViewCustomization(
            showFlag = true,
            showCountryIso = true,
            showCountryName = true,
            showCountryCode = true
        ),
        pickerCustomization = PickerCustomization(
            showFlag = false,
        ),
        showSheet = true
    )
}
```

### Country Code Picker with Text Field

You can integrate the `CountryCodePickerTextField` widget to allow users to input phone numbers while selecting a country code. This combines both the country picker and a text field in one component.

```kotlin
@Composable
fun ShowCountryCodePickerTextField() {
    var text by remember { mutableStateOf("") }
    var country by remember { mutableStateOf(Country.India) }

    // Automatically fetch country based on device locale
    if (!LocalInspectionMode.current) {
        CCPUtils.getCountryAutomatically(context = LocalContext.current)?.let {
            country = it
        }
    }

    CountryCodePickerTextField(
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        textStyle = MaterialTheme.typography.bodyMedium,
        trailingIcon = {
            IconButton(onClick = { text = "" }) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
            }
        },
        label = {
            Text(text = "Phone Number", style = MaterialTheme.typography.bodyMedium)
        },
        showError = true,
        shape = RoundedCornerShape(10.dp),
        onValueChange = { _, value, _ -> text = value },
        number = text,
        showSheet = true,
        selectedCountry = country
    )
}
```

### Phone Number Validation with Country Code Picker

You can also use the `CCPValidator` to validate phone numbers based on the selected country. This feature ensures that users enter valid phone numbers for the selected country.

```kotlin
@Composable
fun ShowCCPWithTextField() {
    var text by remember { mutableStateOf("") }
    var country by remember { mutableStateOf(Country.UnitedStates) }
    
    val context = LocalContext.current
    val validatePhoneNumber = remember { CCPValidator(context = context) }
    
    var isNumberValid by rememberSaveable(country, text) {
        mutableStateOf(validatePhoneNumber(number = text, countryCode = country.countryCode))
    }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            isNumberValid = validatePhoneNumber(number = it, countryCode = country.countryCode)
        },
        modifier = Modifier.fillMaxWidth().padding(16.dp, 5.dp),
        textStyle = MaterialTheme.typography.bodyMedium,
        label = { Text(text = "Phone Number", style = MaterialTheme.typography.bodyMedium) },
        leadingIcon = {
            CountryCodePicker(
                selectedCountry = country,
                countryList = Country.getAllCountries(),
                onCountrySelected = { country = it }
            )
        },
        isError = !isNumberValid && text.isNotEmpty(),
        visualTransformation = CCPTransformer(context, country.countryIso)
    )
}
```

### Customization

The library provides customization options to modify the appearance of the picker and text field:

1. **View Customization**: Allows you to control the visibility of flags, country codes, country names, and more.
   
   Example:

   ```kotlin
   viewCustomization = ViewCustomization(
       showFlag = true,
       showCountryIso = true,
       showCountryName = false,
       showCountryCode = true
   )
   ```

2. **Picker Customization**: Customize the picker’s appearance, such as showing or hiding the flag.

   Example:

   ```kotlin
   pickerCustomization = PickerCustomization(
       showFlag = false,
   )
   ```

3. **Text Field Customization**: Customize the text field with trailing icons, labels, and shapes.

   Example:

   ```kotlin
   CountryCodePickerTextField(
       textStyle = MaterialTheme.typography.bodyMedium,
       trailingIcon = { IconButton(onClick = { text = "" }) { Icon(Icons.Default.Clear, "Clear") } },
       label = { Text("Phone Number", style = MaterialTheme.typography.bodyMedium) },
       shape = RoundedCornerShape(10.dp)
   )
   ```

## Classes and Components

### `CountryCodePicker`
The primary widget for country code selection. It displays a picker or dialog for selecting a country code, along with other country details.

### `CountryCodePickerTextField`
A composable widget that combines a country picker and a phone number input field into one unified component.

### `CCPUtils`
A utility class that helps with various country code-related operations such as automatically fetching the country based on the device's locale.

### `CCPValidator`
A utility class to validate phone numbers based on the selected country code.

### `ViewCustomization`
A data class to customize the visibility of various country details (flag, country name, country code, etc.).

### `PickerCustomization`
A data class for customizing the appearance of the country code picker (e.g., show/hide flag).
