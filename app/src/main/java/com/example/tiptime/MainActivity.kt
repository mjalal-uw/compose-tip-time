package com.example.tiptime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptime.ui.theme.TipTimeTheme
import java.text.NumberFormat
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipTimeTheme {
                TipTimeLayout()
            }
        }
    }
}

@VisibleForTesting
@Composable
internal fun TipTimeLayout(modifier: Modifier = Modifier) {
    var inputAmount by remember { mutableStateOf("") }
    var inputTipPercent by remember { mutableStateOf("") }
    var roundUp by remember { mutableStateOf(false) }

    val amount = inputAmount.toDoubleOrNull() ?: 0.0
    val tipPercent = inputTipPercent.toDoubleOrNull() ?: 0.0
    val tip = calculateTip(amount, tipPercent, roundUp)
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(40.dp)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .safeDrawingPadding()
        ) {
            Text(
                text = stringResource(R.string.calculate_tip),
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 16.dp)
                    .align(alignment = Alignment.Start)
            )
            EditNumberField(
                value = inputAmount,
                onValueChange = { newInputAmount ->
                    inputAmount = newInputAmount.filter { it.isDigit() || it == '.' }
                },
                label = R.string.bill_amount,
                leadingIcon = R.drawable.money,
                actionButton = ImeAction.Next,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )
            EditNumberField(
                value = inputTipPercent,
                onValueChange = { newInputTipPercent ->
                    inputTipPercent = newInputTipPercent.filter { it.isDigit() || it == '.' }
                },
                actionButton = ImeAction.Done,
                label = R.string.how_was_the_service,
                leadingIcon = R.drawable.percent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )
            RoundTheTipRow(
                roundUp, { roundUp = it }, modifier = Modifier.padding(bottom = 32.dp)
            )
            Text(
                text = stringResource(R.string.tip_amount, tip),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}

@Composable
private fun EditNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    actionButton: ImeAction,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        leadingIcon = { Icon(painter = painterResource(leadingIcon), null) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number, imeAction = actionButton
        ),
        singleLine = true,
        modifier = modifier.wrapContentSize(Alignment.Center)
    )
}

@Composable
private fun RoundTheTipRow(
    roundUp: Boolean, onRoundUpChanged: (Boolean) -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .size(48.dp),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Text(stringResource(R.string.round_up_tip), modifier = Modifier)
        Switch(
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}

@VisibleForTesting
internal fun calculateTip(billAmount: Double, tipPercent: Double, roundUp: Boolean): String {
    val tip = tipPercent / 100 * billAmount
    return if (!roundUp) NumberFormat.getCurrencyInstance()
        .format(tip) else NumberFormat.getCurrencyInstance().format(tip.roundToInt())
}

@Preview(showBackground = true)
@Composable
fun TipTimePreview() {
    TipTimeTheme {
        TipTimeLayout()
    }
}