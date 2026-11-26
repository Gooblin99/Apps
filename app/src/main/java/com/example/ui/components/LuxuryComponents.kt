package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BlueTileBorder
import com.example.ui.theme.BlueTileSurface
import com.example.ui.theme.BlueTileText
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDeep
import com.example.ui.theme.GreenTileBorder
import com.example.ui.theme.GreenTileSurface
import com.example.ui.theme.GreenTileText
import com.example.ui.theme.IvoryText
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.MutedPlatinum
import com.example.ui.theme.ObsidianCard
import com.example.ui.theme.ObsidianElevated
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianVoid
import com.example.ui.theme.RedTileBorder
import com.example.ui.theme.RedTileSurface
import com.example.ui.theme.RedTileText
import com.example.ui.theme.YellowTileBorder
import com.example.ui.theme.YellowTileSurface
import com.example.ui.theme.YellowTileText

val GoldMetallicGradient = Brush.linearGradient(
    colors = listOf(GoldBright, ChampagneGold, GoldDeep)
)

val GoldBorderGradient = Brush.linearGradient(
    colors = listOf(
        GoldBright.copy(alpha = 0.7f),
        ChampagneGold.copy(alpha = 0.3f),
        GoldDeep.copy(alpha = 0.6f),
    )
)

@Composable
fun LuxuryVaultBackdrop(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianVoid)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_ceo_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.62f),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            ObsidianVoid.copy(alpha = 0.35f),
                            ObsidianVoid.copy(alpha = 0.55f),
                            ObsidianVoid.copy(alpha = 0.82f),
                        )
                    )
                )
        )
    }
}

@Composable
fun LuxuryEmblemBadge(
    size: Dp = 72.dp,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(ObsidianSurface)
            .border(width = 2.dp, brush = GoldMetallicGradient, shape = CircleShape)
            .padding(4.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_angka_bandar_fg_asset),
            contentDescription = "Lambang ANGKA BANDAR",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
        )
    }
}

@Composable
fun LuxuryCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = ObsidianCard.copy(alpha = 0.78f),
        border = BorderStroke(1.dp, GoldBorderGradient),
        tonalElevation = 6.dp,
        shadowElevation = 12.dp,
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            content = content,
        )
    }
}

@Composable
fun GoldPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = ObsidianVoid,
            disabledContainerColor = ObsidianElevated,
            disabledContentColor = MutedPlatinum,
        ),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .minimumInteractiveComponentSize()
            .height(52.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(
                brush = if (enabled) GoldMetallicGradient else Brush.linearGradient(
                    listOf(ObsidianElevated, ObsidianElevated)
                )
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 20.dp),
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(19.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}

@Composable
fun GoldOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            width = 1.2.dp,
            color = if (enabled) ChampagneGold.copy(alpha = 0.7f) else MutedPlatinum.copy(alpha = 0.25f),
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = ObsidianSurface.copy(alpha = 0.8f),
            contentColor = GoldBright,
            disabledContentColor = MutedPlatinum.copy(alpha = 0.5f),
        ),
        modifier = modifier
            .minimumInteractiveComponentSize()
            .height(52.dp),
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun FourDigitSlotsRow(
    digits: String,
    onSlotClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val labels = listOf("AS", "KOP", "KEPALA", "EKOR")
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSlotClick),
    ) {
        labels.forEachIndexed { index, label ->
            val char = digits.getOrNull(index)
            val isFilled = char != null
            val isActive = index == digits.length && digits.length < 4
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isFilled) ObsidianElevated else ObsidianSurface,
                border = BorderStroke(
                    width = if (isActive || isFilled) 1.5.dp else 1.dp,
                    brush = if (isFilled || isActive) GoldMetallicGradient else Brush.linearGradient(
                        listOf(
                            MutedPlatinum.copy(alpha = 0.25f),
                            MutedPlatinum.copy(alpha = 0.25f),
                        )
                    ),
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(74.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(
                        text = char?.toString() ?: "•",
                        fontFamily = JetBrainsMonoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        color = if (isFilled) GoldBright else MutedPlatinum.copy(alpha = 0.45f),
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 9.sp,
                        color = if (isFilled) ChampagneGold else MutedPlatinum.copy(alpha = 0.7f),
                    )
                }
            }
        }
    }
}

enum class LineBoxTone {
    NEUTRAL,
    RED_TRANSPARENT,
    GREEN_TRANSPARENT,
    YELLOW_TRANSPARENT,
    BLUE_TRANSPARENT,
}

@Composable
fun PredictionLineTile(
    lineIndex: Int,
    number2D: String,
    tone: LineBoxTone = LineBoxTone.NEUTRAL,
    showTrailingStar: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val containerColor = when (tone) {
        LineBoxTone.NEUTRAL -> ObsidianElevated
        LineBoxTone.RED_TRANSPARENT -> RedTileSurface
        LineBoxTone.GREEN_TRANSPARENT -> GreenTileSurface
        LineBoxTone.YELLOW_TRANSPARENT -> YellowTileSurface
        LineBoxTone.BLUE_TRANSPARENT -> BlueTileSurface
    }
    val borderStroke = when (tone) {
        LineBoxTone.NEUTRAL -> BorderStroke(1.dp, GoldBorderGradient)
        LineBoxTone.RED_TRANSPARENT -> BorderStroke(1.1.dp, RedTileBorder)
        LineBoxTone.GREEN_TRANSPARENT -> BorderStroke(1.1.dp, GreenTileBorder)
        LineBoxTone.YELLOW_TRANSPARENT -> BorderStroke(1.1.dp, YellowTileBorder)
        LineBoxTone.BLUE_TRANSPARENT -> BorderStroke(1.1.dp, BlueTileBorder)
    }
    val indexColor = when (tone) {
        LineBoxTone.NEUTRAL -> MutedPlatinum
        LineBoxTone.RED_TRANSPARENT -> RedTileText.copy(alpha = 0.8f)
        LineBoxTone.GREEN_TRANSPARENT -> GreenTileText.copy(alpha = 0.8f)
        LineBoxTone.YELLOW_TRANSPARENT -> YellowTileText.copy(alpha = 0.8f)
        LineBoxTone.BLUE_TRANSPARENT -> BlueTileText.copy(alpha = 0.8f)
    }
    val numberColor = when (tone) {
        LineBoxTone.NEUTRAL -> GoldBright
        LineBoxTone.RED_TRANSPARENT -> RedTileText
        LineBoxTone.GREEN_TRANSPARENT -> GreenTileText
        LineBoxTone.YELLOW_TRANSPARENT -> YellowTileText
        LineBoxTone.BLUE_TRANSPARENT -> BlueTileText
    }
    val innerGlow = when (tone) {
        LineBoxTone.NEUTRAL -> ChampagneGold.copy(alpha = 0.09f)
        LineBoxTone.RED_TRANSPARENT -> RedTileBorder.copy(alpha = 0.12f)
        LineBoxTone.GREEN_TRANSPARENT -> GreenTileBorder.copy(alpha = 0.12f)
        LineBoxTone.YELLOW_TRANSPARENT -> YellowTileBorder.copy(alpha = 0.12f)
        LineBoxTone.BLUE_TRANSPARENT -> BlueTileBorder.copy(alpha = 0.12f)
    }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = containerColor,
        border = borderStroke,
        shadowElevation = 0.dp,
        modifier = modifier.height(50.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            innerGlow,
                            Color.Transparent,
                        )
                    )
                )
                .padding(horizontal = 3.dp, vertical = 4.dp),
        ) {
            Text(
                text = String.format("L%03d", lineIndex + 1),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
                color = indexColor,
                modifier = Modifier.align(Alignment.TopCenter),
            )
            Text(
                text = if (showTrailingStar) "$number2D*" else number2D,
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = if (showTrailingStar) 17.sp else 19.sp,
                color = numberColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}
