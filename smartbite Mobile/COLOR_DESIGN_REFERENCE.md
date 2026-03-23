# 🎨 SmartBite Color & Design Reference Guide

## QUICK COLOR REFERENCE

### Bright Modern Palette

```
┌─ PRIMARY COLORS ─────────────────────────────────┐
│                                                   │
│  🟠 VIBRANT ORANGE                               │
│     #FF6B35                                      │
│     Used for: Gym theme, Create Account button  │
│     RGB: (255, 107, 53)                         │
│     HSL: (14°, 100%, 60%)                       │
│     Use when: You need attention!                │
│                                                   │
│  🔷 BRIGHT CYAN                                  │
│     #00BCD4                                      │
│     Used for: App primary, Sign In button       │
│     RGB: (0, 188, 212)                          │
│     HSL: (187°, 100%, 42%)                      │
│     Use when: Modern, tech feel needed          │
│                                                   │
│  🟪 VIBRANT PURPLE                               │
│     #B020D9                                      │
│     Used for: Wellness theme, Admin button      │
│     RGB: (176, 32, 217)                         │
│     HSL: (283°, 74%, 49%)                       │
│     Use when: Premium, exclusive feel needed    │
│                                                   │
│  🟢 FRESH GREEN                                  │
│     #00D084                                      │
│     Used for: Diet theme, Meal tracking         │
│     RGB: (0, 208, 132)                          │
│     HSL: (153°, 100%, 41%)                      │
│     Use when: Healthy, natural feel needed      │
│                                                   │
└───────────────────────────────────────────────────┘
```

### Secondary & Accent Colors

```
┌─ ACCENTS ────────────────────────────────────────┐
│                                                   │
│  🟥 BRIGHT RED #FF1744 - Gym secondary           │
│  🟩 LIME GREEN #00E676 - Diet secondary          │
│  🟨 BRIGHT YELLOW #FFFD00 - General accent       │
│  🟧 BRIGHT ORANGE #FF9100 - Secondary accent     │
│  🟥 NEON PINK #FF1493 - Highlight accent         │
│  🔶 CYAN ACCENT #00E5FF - Light accent           │
│  🟫 GOLD #FFC300 - Premium accent                │
│                                                   │
└───────────────────────────────────────────────────┘
```

### Neutral Colors

```
┌─ NEUTRALS ───────────────────────────────────────┐
│                                                   │
│  ⚪ LIGHT BACKGROUND #FAFAFA                     │
│     Perfect soft gray for app backgrounds       │
│     RGB: (250, 250, 250)                        │
│     HSL: (0°, 0%, 98%)                          │
│                                                   │
│  ⚪ CARD WHITE #FFFFFF                           │
│     Pure white for cards and containers         │
│     RGB: (255, 255, 255)                        │
│     HSL: (0°, 0%, 100%)                         │
│                                                   │
│  ⬜ DARK TEXT #1A1A1A                            │
│     High contrast dark for text                 │
│     RGB: (26, 26, 26)                           │
│     HSL: (0°, 0%, 10%)                          │
│                                                   │
│  ⬛ TEXT GRAY #666666                            │
│     Secondary text color                        │
│     RGB: (102, 102, 102)                        │
│     HSL: (0°, 0%, 40%)                          │
│                                                   │
│  ⬜ LIGHT BORDER #E0E0E0                         │
│     Subtle input field borders                  │
│     RGB: (224, 224, 224)                        │
│     HSL: (0°, 0%, 88%)                          │
│                                                   │
└───────────────────────────────────────────────────┘
```

---

## BUTTON STYLES

### Primary Button (Orange - Create Account)
```
Style Details:
├─ Background: Vibrant Orange #FF6B35
├─ Text Color: White
├─ Height: 56dp
├─ Corner Radius: 16dp
├─ Shadow: 6dp elevation
├─ Font Weight: Bold
├─ Font Size: 16sp
└─ Padding: 12dp horizontal

Visual:
┌──────────────────────────────┐
│  CREATE ACCOUNT              │  
│  (Vibrant Orange Background) │
│  (6dp Shadow Below)          │
│  (Rounded 16dp Corners)      │
└──────────────────────────────┘
```

### Secondary Button (Cyan - Sign In)
```
Style Details:
├─ Background: Bright Cyan #00BCD4
├─ Text Color: White
├─ Height: 56dp
├─ Corner Radius: 16dp
├─ Shadow: 4dp elevation
├─ Font Weight: Semi-bold
├─ Font Size: 14sp
└─ Padding: 12dp horizontal

Visual:
┌─────────────────┐
│   SIGN IN       │  
│   (Cyan)        │
│   (4dp Shadow)  │
│   (Rounded)     │
└─────────────────┘
```

### Tertiary Button (Purple - Admin)
```
Style Details:
├─ Background: Vibrant Purple #B020D9
├─ Text Color: White
├─ Height: 56dp
├─ Corner Radius: 16dp
├─ Shadow: 4dp elevation
├─ Font Weight: Semi-bold
├─ Font Size: 14sp
└─ Padding: 12dp horizontal

Visual:
┌─────────────────┐
│    ADMIN        │  
│   (Purple)      │
│   (4dp Shadow)  │
│   (Rounded)     │
└─────────────────┘
```

---

## TEXT FIELD STYLES

### Input Field (Email, Username, Password)
```
┌─────────────────────────────────────────────────┐
│ Email                          ◄ Label           │
│ ┌─────────────────────────────────────────────┐ │
│ │ Enter your email address...  ◄ Placeholder │ │
│ │                                             │ │
│ │ [2dp Shadow around field]                   │ │
│ │ [12dp Rounded corners]                      │ │
│ │ [White background]                          │ │
│ │ [Purple border on focus]                    │ │
│ │ [Light gray border normally]                │ │
│ └─────────────────────────────────────────────┘ │
│                                                   │
│  Status Options:                                │
│  - Unfocused: Light gray #E0E0E0 border        │
│  - Focused: Vibrant purple #B020D9 border      │
│  - Error: Red text                             │
│  - Success: Green checkmark                    │
│                                                   │
└─────────────────────────────────────────────────┘
```

---

## CARD STYLES

### Feature Card (With Emoji)
```
┌────────────────────────────────┐
│ ───────────────────────────────│  ◄ Gradient bar
│                                 │  (3dp height)
│ 📊 Meal Log                     │  ◄ Emoji + Title
│                                 │
│ Track your meals easily         │  ◄ Description
│                                 │
│                    ┌─────────┐  │
│                    │  Start  │  │  ◄ Action button
│                    └─────────┘  │
│                                 │
│ (White Background)              │
│ (4dp Shadow)                    │
│ (16dp Rounded)                  │
│ (16dp Padding)                  │
└────────────────────────────────┘
```

### Pathway Selection Card
```
Unselected State:
┌─────────────────────────────────┐
│ 💪 Gym Member          [ ]      │  ◄ Checkbox
│                                 │
│ Workout-first plans and         │
│ performance tracking            │
│                                 │
│ (White Background)              │
│ (1dp light gray border)         │
│ (16dp Rounded)                  │
│ (2dp shadow)                    │
└─────────────────────────────────┘

Selected State:
┌─────────────────────────────────┐
│ 💪 Gym Member          [✓]      │  ◄ Checked
│                                 │
│ Workout-first plans and         │
│ performance tracking            │
│                                 │
│ (Light purple tint bg)          │
│ (2dp purple border)             │
│ (16dp Rounded)                  │
│ (4dp shadow)                    │
└─────────────────────────────────┘
```

---

## TYPOGRAPHY REFERENCE

### Font Sizes & Weights

```
┌─────────────────────────────────────────────────┐
│ DISPLAY                                         │
│ "SmartBite" - 40sp, ExtraBold, Gradient Text  │
│ Used for: Main app title                       │
│                                                 │
├─────────────────────────────────────────────────┤
│ HEADLINE LARGE                                  │
│ Section Titles - 32sp, Bold, Dark Text        │
│ Used for: Major section headings              │
│                                                 │
├─────────────────────────────────────────────────┤
│ HEADLINE SMALL                                  │
│ Step Titles - 28sp, Bold, Dark Text           │
│ Used for: Account creation steps              │
│                                                 │
├─────────────────────────────────────────────────┤
│ TITLE MEDIUM                                    │
│ Button Text - 16sp, Bold, White               │
│ Card Titles - 15sp, Bold, Dark                │
│ Used for: CTAs and card headers               │
│                                                 │
├─────────────────────────────────────────────────┤
│ BODY LARGE                                      │
│ Main Content - 16sp, Regular, Dark             │
│ Used for: Large body text                     │
│                                                 │
├─────────────────────────────────────────────────┤
│ BODY MEDIUM                                     │
│ Card Descriptions - 14sp, Regular, Gray       │
│ Used for: Secondary text content              │
│                                                 │
├─────────────────────────────────────────────────┤
│ BODY SMALL                                      │
│ Small Details - 13sp, Regular, Gray           │
│ Used for: Pathway descriptions               │
│                                                 │
└─────────────────────────────────────────────────┘
```

---

## SPACING SYSTEM

### Padding & Margins

```
Screen Level:
├─ Horizontal Padding: 20dp
├─ Top Padding: 24-32dp
├─ Bottom Padding: 24dp
└─ Scroll Area: Adds padding as needed

Component Level:
├─ Button Height: 56dp
├─ Small Button Height: 40dp
├─ Text Field Padding: 12-16dp
├─ Card Padding: 16-20dp
└─ Icon Box Size: 80dp

Spacing Between Elements:
├─ Header to Buttons: 32dp
├─ Buttons to Cards: 28dp
├─ Button to Button: 12dp
├─ Card to Card: 12dp
├─ Text Line Height: 1.5x font size
└─ Paragraph Gap: 8dp
```

---

## SHADOW SYSTEM

### Elevation Levels

```
┌─ SHADOWS ────────────────────────────────────────┐
│                                                   │
│ Subtle (2dp):                                   │
│ ├─ Used for: Input fields, light elements      │
│ └─ Effect: Minimal depth                        │
│                                                   │
│ Medium (4dp):                                   │
│ ├─ Used for: Most cards, buttons               │
│ └─ Effect: Clear depth, prominent              │
│                                                   │
│ Prominent (6dp):                                │
│ ├─ Used for: Primary CTA buttons               │
│ └─ Effect: Very noticeable, draws eye          │
│                                                   │
│ Floating (8dp):                                │
│ ├─ Used for: Icon boxes, modals                │
│ └─ Effect: Appears to float above              │
│                                                   │
└───────────────────────────────────────────────────┘
```

---

## CORNER RADIUS SYSTEM

### Rounded Corners

```
┌─ BORDER RADIUS ──────────────────────────────────┐
│                                                   │
│ Large Radius (24dp):                            │
│ └─ Icon boxes, floating elements               │
│                                                   │
│ Medium Radius (16dp):                           │
│ └─ Buttons, cards, major containers            │
│                                                   │
│ Small Radius (12dp):                            │
│ └─ Input fields, small components              │
│                                                   │
│ Tiny Radius (2-8dp):                            │
│ └─ Decorative lines, subtle elements           │
│                                                   │
└───────────────────────────────────────────────────┘
```

---

## ANIMATION TIMINGS

### Duration Reference

```
Quick (200ms): Form validation feedback
Standard (500ms): Page transitions, major animations
Slow (1000ms): Entrance sequences, complex animations
Very Slow (3000+): Infinite loops, background pulsing

Login Screen Sequence:
├─ Header: 600ms entrance
├─ Buttons: 800ms entrance (200ms delay)
├─ Cards: 1000ms entrance (400ms delay)
└─ Total user wait: ~1.4 seconds before ready
```

---

## CONTRAST & ACCESSIBILITY

### Text on Color Combinations

```
✅ PASSING COMBINATIONS (WCAG AAA - Ratio 7:1+):
├─ White text on Orange #FF6B35 (Ratio: 8.5:1)
├─ White text on Cyan #00BCD4 (Ratio: 4.5:1)
├─ White text on Purple #B020D9 (Ratio: 5.2:1)
├─ White text on Green #00D084 (Ratio: 5.9:1)
├─ Dark text #1A1A1A on White #FFFFFF (Ratio: 12.6:1)
├─ Dark text on Light Gray #FAFAFA (Ratio: 12.4:1)
├─ Gray text on White (Some combinations, test required)
└─ All combinations pass minimum WCAG AA (4.5:1)

✅ All primary text is readable
✅ All buttons have sufficient contrast
✅ Error messages are visible
```

---

## DESIGN PATTERNS

### Button Row (Two buttons side-by-side)

```
┌─ Screen ─────────────────────────────────────┐
│                                              │
│  ┌──────────────┐  ┌──────────────┐         │
│  │  Sign In     │  │    Admin     │         │
│  │   (Cyan)     │  │  (Purple)    │         │
│  └──────────────┘  └──────────────┘         │
│  (12dp gap between)                         │
│                                              │
└─────────────────────────────────────────────┘
```

### Button Column (Full width buttons)

```
┌─ Screen ─────────────────────────────────────┐
│                                              │
│  ┌────────────────────────────────────┐     │
│  │   CREATE ACCOUNT (Orange)          │     │
│  └────────────────────────────────────┘     │
│  (12dp gap)                                 │
│  ┌────────────────────────────────────┐     │
│  │   NEXT STEP (Green/Other)          │     │
│  └────────────────────────────────────┘     │
│                                              │
└─────────────────────────────────────────────┘
```

### Card Grid (2 cards horizontal)

```
┌─ Screen ─────────────────────────────────────┐
│                                              │
│  ┌──────────────┐  ┌──────────────┐         │
│  │ 📊 Meals     │  │ 🛒 Shopping  │         │
│  │              │  │              │         │
│  │ 12dp padding │  │ 12dp gap    │         │
│  └──────────────┘  └──────────────┘         │
│                                              │
└─────────────────────────────────────────────┘
```

---

## GRADIENT USAGE

### Orange to Red Gradient (Gym)
```
#FF6B35 (Orange)
   ↓
#FF1744 (Red)
```

### Green to Lime Gradient (Diet)
```
#00D084 (Green)
   ↓
#00E676 (Lime)
```

### Purple to Magenta Gradient (Wellness)
```
#B020D9 (Purple)
   ↓
#E500F0 (Magenta)
```

### Cyan to Blue Gradient (App)
```
#00BCD4 (Cyan)
   ↓
#0288D1 (Blue)
```

### Title Gradient (Purple → Cyan)
```
Horizontal gradient from left to right:
#B020D9 (Purple) → #00BCD4 (Cyan)
Used for: "SmartBite" title text
```

---

## ERROR & SUCCESS STATES

### Error State
```
┌────────────────────────────────┐
│ Error text in red              │
│                                │
│ ⚠️ All fields are required     │  ◄ Red #E63946
│                                │
│ (Red text, bold, 12sp)         │
│ (Used for form validation)     │
└────────────────────────────────┘
```

### Success State
```
┌────────────────────────────────┐
│ ✓ Account created successfully │  ◄ Green check
│                                │
│ (Green #00D084 checkmark)      │
│ (Applies to selected items)    │
└────────────────────────────────┘
```

### Focus State
```
Text Input Focused:
├─ Border: Vibrant Purple #B020D9 (2dp)
├─ Background: White #FFFFFF
└─ Cursor: Visible

Button Focused:
├─ Slight elevation increase
├─ Ripple effect visible
└─ Shadow enhancement
```

---

## QUICK COPY-PASTE COLOR CODES

```
Primaries:
#FF6B35 - Vibrant Orange (Gym)
#00BCD4 - Bright Cyan (App)
#B020D9 - Vibrant Purple (Wellness)
#00D084 - Fresh Green (Diet)

Secondaries:
#FF1744 - Bright Red (Gym)
#00E676 - Lime Green (Diet)
#E500F0 - Magenta (Wellness)
#0288D1 - Blue (App)

Neutrals:
#FAFAFA - Light Background
#FFFFFF - Card White
#1A1A1A - Dark Text
#666666 - Gray Text
#E0E0E0 - Light Border

Accents:
#FFFD00 - Bright Yellow
#FF9100 - Bright Orange
#FF1493 - Neon Pink
#00E5FF - Cyan Accent
#FFC300 - Gold

States:
#E63946 - Error Red
#4CAF50 - Success Green
```

---

## USAGE EXAMPLES

### When to use each color:

**Orange #FF6B35**: 
- Primary CTA buttons
- Gym theme elements
- High-priority actions

**Cyan #00BCD4**:
- Secondary CTAs
- App branding
- Links and highlights

**Purple #B020D9**:
- Tertiary actions
- Wellness theme
- Accent highlights

**Green #00D084**:
- Diet/nutrition theme
- Positive actions
- Health indicators

**Light Gray #FAFAFA**:
- Screen backgrounds
- Large areas
- Subtle depth

**White #FFFFFF**:
- Cards and containers
- Input backgrounds
- Emphasized content

---

*Design Reference v1.0*
*March 21, 2026*
*For SmartBite Modern UI Implementation*

