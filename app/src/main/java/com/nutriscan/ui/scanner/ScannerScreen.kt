package com.nutriscan.ui.scanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.nutriscan.data.repository.FoodRepository
import com.nutriscan.data.repository.MealRepository
import com.nutriscan.ml.BarcodeScanner
import com.nutriscan.ml.TextRecognizer
import com.nutriscan.ml.NutritionParser
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(
    foodRepository: FoodRepository,
    mealRepository: MealRepository
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    
    var scanMode by remember { mutableStateOf(ScanMode.BARCODE) }
    var scanResult by remember { mutableStateOf<String?>(null) }
    var isScanning by remember { mutableStateOf(false) }
    
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }
    
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Scan Food",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Scan mode selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = scanMode == ScanMode.BARCODE,
                onClick = { scanMode = ScanMode.BARCODE },
                label = { Text("Barcode") }
            )
            FilterChip(
                selected = scanMode == ScanMode.NUTRITION_LABEL,
                onClick = { scanMode = ScanMode.NUTRITION_LABEL },
                label = { Text("Label OCR") }
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (!hasCameraPermission) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Camera permission required")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }
                    ) {
                        Text("Grant Permission")
                    }
                }
            }
        } else {
            // Camera preview would go here
            // For now, showing a placeholder with manual input
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (isScanning) {
                        CircularProgressIndicator()
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = when (scanMode) {
                                    ScanMode.BARCODE -> "Point camera at barcode"
                                    ScanMode.NUTRITION_LABEL -> "Point camera at nutrition label"
                                },
                                style = MaterialTheme.typography.bodyLarge
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Manual test button
                            Button(
                                onClick = {
                                    scope.launch {
                                        isScanning = true
                                        when (scanMode) {
                                            ScanMode.BARCODE -> {
                                                // Test with a known barcode
                                                val food = foodRepository.lookupBarcode("737628064502")
                                                scanResult = food?.name ?: "Not found"
                                            }
                                            ScanMode.NUTRITION_LABEL -> {
                                                scanResult = "OCR functionality ready"
                                            }
                                        }
                                        isScanning = false
                                    }
                                }
                            ) {
                                Text("Test Scan")
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            scanResult?.let { result ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Scan Result:",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(text = result)
                    }
                }
            }
        }
    }
}

enum class ScanMode {
    BARCODE,
    NUTRITION_LABEL
}
