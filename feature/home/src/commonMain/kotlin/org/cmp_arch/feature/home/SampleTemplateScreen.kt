package org.cmp_arch.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.cmp_arch.core.composeutils.screenHorizontalPadding
import org.cmp_arch.designsystem.components.DsPrimaryButton
import org.cmp_arch.designsystem.components.DsSurfaceCard
import org.cmp_arch.designsystem.components.DsTopAppBar

@Composable
fun SampleTemplateScreen(
    onBackRequested: () -> Unit,
) {
    val sections = rememberSampleSections()

    Scaffold(
        topBar = {
            DsTopAppBar(
                title = "Template Sample",
                actions = {
                    DsPrimaryButton(text = "Back", onClick = onBackRequested)
                },
            )
        },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .screenHorizontalPadding()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
        ) {
            items(sections, key = { it.title }) { section ->
                SampleSectionCard(section = section)
            }
        }
    }
}

@Composable
private fun SampleSectionCard(section: SampleSection) {
    DsSurfaceCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = section.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = section.description,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun rememberSampleSections(): List<SampleSection> {
    return listOf(
        SampleSection(
            title = "Presentation Layer",
            description = "MVI state is immutable, intent-driven, and effect-safe.",
        ),
        SampleSection(
            title = "Domain Layer",
            description = "Use cases and repository contracts stay pure and framework-free.",
        ),
        SampleSection(
            title = "Data Layer",
            description = "DTO -> cache -> domain mapping is isolated from UI concerns.",
        ),
        SampleSection(
            title = "Multiplatform",
            description = "Platform details are isolated with expect/actual abstractions.",
        ),
        SampleSection(
            title = "Scalability",
            description = "Feature-first modularization prevents cross-layer leakage.",
        ),
    )
}

@Immutable
private data class SampleSection(
    val title: String,
    val description: String,
)
