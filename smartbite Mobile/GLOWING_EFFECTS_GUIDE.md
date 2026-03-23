/**
 * GLOWING ANIMATED EFFECTS - USAGE GUIDE
 * 
 * Your SmartBite app now has amazing animated glowing effects!
 * Here's how to use them in your screens:
 */

// ============================================================================
// 1. ANIMATED GLOWING GRADIENT EFFECT
// ============================================================================
// Use this for background gradients that have a shimmer/glow effect
// Perfect for: Hero sections, headers, full-screen backgrounds

@Composable
fun MyScreen(pathwayTheme: PathwayTheme) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getAnimatedGlowingGradient(pathwayTheme))
    ) {
        // Your content here
    }
}

// Effects:
// - Shimmer animation that flows across the screen
// - Glowing glow effect
// - Duration: 2 seconds per cycle
// - Perfect for eye-catching backgrounds


// ============================================================================
// 2. ANIMATED GLOWING BACKGROUND MODIFIER
// ============================================================================
// Use this for cards, buttons, or containers that need a glowing border + shadow
// Perfect for: Feature cards, stat cards, call-to-action buttons

@Composable
fun StatCard(pathwayTheme: PathwayTheme) {
    Box(
        modifier = Modifier
            .size(200.dp, 120.dp)
            .animatedGlowingBackground(pathwayTheme, cornerRadius = 16f)
    ) {
        // Card content
    }
}

// Effects:
// - Animated glowing border that pulses
// - Dynamic shadow that grows and shrinks
// - Smooth gradient background
// - Duration: 1.5 seconds
// - Corner radius is customizable


// ============================================================================
// 3. PULSING GLOW EFFECT
// ============================================================================
// Use this for emphasized elements that need to grab attention
// Perfect for: Action buttons, notifications, important metrics

@Composable
fun ImportantMetric(pathwayTheme: PathwayTheme) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .pulsingGlow(pathwayTheme, intensity = 1.5f)
    ) {
        // Metric content
    }
}

// Effects:
// - Pulsing shadow that grows and shrinks
// - Alpha animation for fade effect
// - Creates "breathing" effect
// - Duration: 1 second
// - Intensity multiplier: 1-2 (adjust for more/less glow)


// ============================================================================
// 4. ANIMATED GRADIENT SHIFT
// ============================================================================
// Use this for containers with alternating gradient directions
// Perfect for: Section backgrounds, container backgrounds

@Composable
fun FeatureSection(pathwayTheme: PathwayTheme) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .animatedGradientShift(pathwayTheme, duration = 3000)
    ) {
        // Section content
    }
}

// Effects:
// - Gradient reverses direction smoothly
// - Color swap animation
// - Duration: 3 seconds (customizable)
// - Very smooth and hypnotic effect


// ============================================================================
// 5. APP-WIDE GLOWING GRADIENT
// ============================================================================
// Use this for the main app background (blue with black)
// Perfect for: Main screens, app-level background

@Composable
fun AppBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getAnimatedAppGradient())
    ) {
        // Your entire app content
    }
}

// Effects:
// - Blue to black gradient with shimmer
// - Animated shimmer effect
// - Duration: 2.5 seconds
// - Creates depth and visual appeal


// ============================================================================
// 6. PRE-BUILT GLOWING COMPONENTS
// ============================================================================
// These are ready-to-use components in GlowingComponents.kt

// Glowing Card with title and content
@Composable
fun Example1(pathwayTheme: PathwayTheme) {
    GlowingCard(
        pathwayTheme = pathwayTheme,
        title = "Calories",
        content = "2,500 cal",
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    )
}

// Pulsing Glow Box - great for CTAs
@Composable
fun Example2(pathwayTheme: PathwayTheme) {
    PulsingGlowBox(
        pathwayTheme = pathwayTheme,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Text("Start Your Workout", color = Color.White, fontWeight = FontWeight.Bold)
    }
}

// Gradient Shift Box
@Composable
fun Example3(pathwayTheme: PathwayTheme) {
    GradientShiftBox(
        pathwayTheme = pathwayTheme,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Text("Animated Section", color = Color.White)
    }
}


// ============================================================================
// 7. COMBINING EFFECTS
// ============================================================================
// You can chain modifiers for incredible effects

@Composable
fun SuperGlowingCard(pathwayTheme: PathwayTheme) {
    Box(
        modifier = Modifier
            .size(250.dp)
            .animatedGlowingBackground(pathwayTheme, cornerRadius = 20f)
            .pulsingGlow(pathwayTheme, intensity = 0.8f)
    ) {
        // Doubly glowing card!
    }
}


// ============================================================================
// 8. COLOR SCHEME MAPPING
// ============================================================================

// Gym Pathway (Red → Blue Gradient):
// - GymGradientStart: #E63946 (Red)
// - GymGradientEnd: #457B9D (Blue)

// Diet Pathway (Green → Blue Gradient):
// - DietGradientStart: #06A77D (Green)
// - DietGradientEnd: #1F5A8E (Blue)

// Wellness Pathway (Purple → Blue Gradient):
// - WellnessGradientStart: #7D3C98 (Purple)
// - WellnessGradientEnd: #457B9D (Blue)

// App-wide (Blue → Black Gradient):
// - AppGradientStart: #1E90FF (Blue)
// - AppGradientEnd: #1A1A1A (Black)


// ============================================================================
// 9. PERFORMANCE TIPS
// ============================================================================

// - Use animated effects sparingly to avoid performance issues
// - Don't apply too many glowing effects to the same screen
// - Consider using intensity parameter to control glow strength
// - Test on real devices to ensure smooth animations
// - Remember: Quality over quantity!


// ============================================================================
// 10. QUICK START EXAMPLE FOR DASHBOARD
// ============================================================================

@Composable
fun DashboardWithGlow(
    pathwayTheme: PathwayTheme
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getAnimatedAppGradient())
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                GlowingCard(
                    pathwayTheme = pathwayTheme,
                    title = "Meals Logged",
                    content = "1/3",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
            }
            
            item {
                GlowingCard(
                    pathwayTheme = pathwayTheme,
                    title = "Water Consumed",
                    content = "1500 ml",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
            }
            
            item {
                PulsingGlowBox(
                    pathwayTheme = pathwayTheme,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    Text("Sync Now", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

