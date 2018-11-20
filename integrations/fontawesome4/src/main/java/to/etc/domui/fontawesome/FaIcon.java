package to.etc.domui.fontawesome;

import to.etc.domui.component.misc.FontIcon;
import to.etc.domui.component.misc.IFontIconRef;
import to.etc.domui.component.misc.IIconRef;
import to.etc.domui.component.misc.WrappedIconRef;
import to.etc.domui.dom.html.NodeBase;

/**
 * All of the definitions in the FontAwesome 4.7.0 font distribution.
 *
 * @author <a href="mailto:jal@etc.to">Frits Jalvingh</a>
 * Created on 20-10-18.
 */
public enum FaIcon implements IFontIconRef {
	fa500px("fa-500px"),
	faAddressBook("fa-address-book"),
	faAddressBookO("fa-address-book-o"),
	faAddressCard("fa-address-card"),
	faAddressCardO("fa-address-card-o"),
	faAdjust("fa-adjust"),
	faAdn("fa-adn"),
	faAlignCenter("fa-align-center"),
	faAlignJustify("fa-align-justify"),
	faAlignLeft("fa-align-left"),
	faAlignRight("fa-align-right"),
	faAmazon("fa-amazon"),
	faAmbulance("fa-ambulance"),
	faAmericanSignLanguageInterpreting("fa-american-sign-language-interpreting"),
	faAnchor("fa-anchor"),
	faAndroid("fa-android"),
	faAngellist("fa-angellist"),
	faAngleDoubleDown("fa-angle-double-down"),
	faAngleDoubleLeft("fa-angle-double-left"),
	faAngleDoubleRight("fa-angle-double-right"),
	faAngleDoubleUp("fa-angle-double-up"),
	faAngleDown("fa-angle-down"),
	faAngleLeft("fa-angle-left"),
	faAngleRight("fa-angle-right"),
	faAngleUp("fa-angle-up"),
	faApple("fa-apple"),
	faArchive("fa-archive"),
	faAreaChart("fa-area-chart"),
	faArrowCircleDown("fa-arrow-circle-down"),
	faArrowCircleLeft("fa-arrow-circle-left"),
	faArrowCircleODown("fa-arrow-circle-o-down"),
	faArrowCircleOLeft("fa-arrow-circle-o-left"),
	faArrowCircleORight("fa-arrow-circle-o-right"),
	faArrowCircleOUp("fa-arrow-circle-o-up"),
	faArrowCircleRight("fa-arrow-circle-right"),
	faArrowCircleUp("fa-arrow-circle-up"),
	faArrowDown("fa-arrow-down"),
	faArrowLeft("fa-arrow-left"),
	faArrowRight("fa-arrow-right"),
	faArrowUp("fa-arrow-up"),
	faArrows("fa-arrows"),
	faArrowsAlt("fa-arrows-alt"),
	faArrowsH("fa-arrows-h"),
	faArrowsV("fa-arrows-v"),
	faAssistiveListeningSystems("fa-assistive-listening-systems"),
	faAsterisk("fa-asterisk"),
	faAt("fa-at"),
	faAudioDescription("fa-audio-description"),
	faBackward("fa-backward"),
	faBalanceScale("fa-balance-scale"),
	faBan("fa-ban"),
	faBandcamp("fa-bandcamp"),
	faBarChart("fa-bar-chart"),
	faBarcode("fa-barcode"),
	faBars("fa-bars"),
	faBath("fa-bath"),
	faBatteryEmpty("fa-battery-empty"),
	faBatteryFull("fa-battery-full"),
	faBatteryHalf("fa-battery-half"),
	faBatteryQuarter("fa-battery-quarter"),
	faBatteryThreeQuarters("fa-battery-three-quarters"),
	faBed("fa-bed"),
	faBeer("fa-beer"),
	faBehance("fa-behance"),
	faBehanceSquare("fa-behance-square"),
	faBell("fa-bell"),
	faBellO("fa-bell-o"),
	faBellSlash("fa-bell-slash"),
	faBellSlashO("fa-bell-slash-o"),
	faBicycle("fa-bicycle"),
	faBinoculars("fa-binoculars"),
	faBirthdayCake("fa-birthday-cake"),
	faBitbucket("fa-bitbucket"),
	faBitbucketSquare("fa-bitbucket-square"),
	faBlackTie("fa-black-tie"),
	faBlind("fa-blind"),
	faBluetooth("fa-bluetooth"),
	faBluetoothB("fa-bluetooth-b"),
	faBold("fa-bold"),
	faBolt("fa-bolt"),
	faBomb("fa-bomb"),
	faBook("fa-book"),
	faBookmark("fa-bookmark"),
	faBookmarkO("fa-bookmark-o"),
	faBraille("fa-braille"),
	faBriefcase("fa-briefcase"),
	faBtc("fa-btc"),
	faBug("fa-bug"),
	faBuilding("fa-building"),
	faBuildingO("fa-building-o"),
	faBullhorn("fa-bullhorn"),
	faBullseye("fa-bullseye"),
	faBus("fa-bus"),
	faBuysellads("fa-buysellads"),
	faCalculator("fa-calculator"),
	faCalendar("fa-calendar"),
	faCalendarCheckO("fa-calendar-check-o"),
	faCalendarMinusO("fa-calendar-minus-o"),
	faCalendarO("fa-calendar-o"),
	faCalendarPlusO("fa-calendar-plus-o"),
	faCalendarTimesO("fa-calendar-times-o"),
	faCamera("fa-camera"),
	faCameraRetro("fa-camera-retro"),
	faCar("fa-car"),
	faCaretDown("fa-caret-down"),
	faCaretLeft("fa-caret-left"),
	faCaretRight("fa-caret-right"),
	faCaretSquareODown("fa-caret-square-o-down"),
	faCaretSquareOLeft("fa-caret-square-o-left"),
	faCaretSquareORight("fa-caret-square-o-right"),
	faCaretSquareOUp("fa-caret-square-o-up"),
	faCaretUp("fa-caret-up"),
	faCartArrowDown("fa-cart-arrow-down"),
	faCartPlus("fa-cart-plus"),
	faCc("fa-cc"),
	faCcAmex("fa-cc-amex"),
	faCcDinersClub("fa-cc-diners-club"),
	faCcDiscover("fa-cc-discover"),
	faCcJcb("fa-cc-jcb"),
	faCcMastercard("fa-cc-mastercard"),
	faCcPaypal("fa-cc-paypal"),
	faCcStripe("fa-cc-stripe"),
	faCcVisa("fa-cc-visa"),
	faCertificate("fa-certificate"),
	faChain("fa-chain"),
	faChainBroken("fa-chain-broken"),
	faCheck("fa-check"),
	faCheckCircle("fa-check-circle"),
	faCheckCircleO("fa-check-circle-o"),
	faCheckSquare("fa-check-square"),
	faCheckSquareO("fa-check-square-o"),
	faChevronCircleDown("fa-chevron-circle-down"),
	faChevronCircleLeft("fa-chevron-circle-left"),
	faChevronCircleRight("fa-chevron-circle-right"),
	faChevronCircleUp("fa-chevron-circle-up"),
	faChevronDown("fa-chevron-down"),
	faChevronLeft("fa-chevron-left"),
	faChevronRight("fa-chevron-right"),
	faChevronUp("fa-chevron-up"),
	faChild("fa-child"),
	faChrome("fa-chrome"),
	faCircle("fa-circle"),
	faCircleO("fa-circle-o"),
	faCircleONotch("fa-circle-o-notch"),
	faCircleThin("fa-circle-thin"),
	faClipboard("fa-clipboard"),
	faClockO("fa-clock-o"),
	faClone("fa-clone"),
	faCloud("fa-cloud"),
	faCloudDownload("fa-cloud-download"),
	faCloudUpload("fa-cloud-upload"),
	faCode("fa-code"),
	faCodeFork("fa-code-fork"),
	faCodepen("fa-codepen"),
	faCodiepie("fa-codiepie"),
	faCoffee("fa-coffee"),
	faCog("fa-cog"),
	faCogs("fa-cogs"),
	faColumns("fa-columns"),
	faComment("fa-comment"),
	faCommentO("fa-comment-o"),
	faCommenting("fa-commenting"),
	faCommentingO("fa-commenting-o"),
	faComments("fa-comments"),
	faCommentsO("fa-comments-o"),
	faCompass("fa-compass"),
	faCompress("fa-compress"),
	faConnectdevelop("fa-connectdevelop"),
	faContao("fa-contao"),
	faCopyright("fa-copyright"),
	faCreativeCommons("fa-creative-commons"),
	faCreditCard("fa-credit-card"),
	faCreditCardAlt("fa-credit-card-alt"),
	faCrop("fa-crop"),
	faCrosshairs("fa-crosshairs"),
	faCss3("fa-css3"),
	faCube("fa-cube"),
	faCubes("fa-cubes"),
	faCutlery("fa-cutlery"),
	faDashcube("fa-dashcube"),
	faDatabase("fa-database"),
	faDeaf("fa-deaf"),
	faDelicious("fa-delicious"),
	faDesktop("fa-desktop"),
	faDeviantart("fa-deviantart"),
	faDiamond("fa-diamond"),
	faDigg("fa-digg"),
	faDotCircleO("fa-dot-circle-o"),
	faDownload("fa-download"),
	faDribbble("fa-dribbble"),
	faDropbox("fa-dropbox"),
	faDrupal("fa-drupal"),
	faEdge("fa-edge"),
	faEercast("fa-eercast"),
	faEject("fa-eject"),
	faEllipsisH("fa-ellipsis-h"),
	faEllipsisV("fa-ellipsis-v"),
	faEmpire("fa-empire"),
	faEnvelope("fa-envelope"),
	faEnvelopeO("fa-envelope-o"),
	faEnvelopeOpen("fa-envelope-open"),
	faEnvelopeOpenO("fa-envelope-open-o"),
	faEnvelopeSquare("fa-envelope-square"),
	faEnvira("fa-envira"),
	faEraser("fa-eraser"),
	faEtsy("fa-etsy"),
	faEur("fa-eur"),
	faExchange("fa-exchange"),
	faExclamation("fa-exclamation"),
	faExclamationCircle("fa-exclamation-circle"),
	faExclamationTriangle("fa-exclamation-triangle"),
	faExpand("fa-expand"),
	faExpeditedssl("fa-expeditedssl"),
	faExternalLink("fa-external-link"),
	faExternalLinkSquare("fa-external-link-square"),
	faEye("fa-eye"),
	faEyeSlash("fa-eye-slash"),
	faEyedropper("fa-eyedropper"),
	faFacebook("fa-facebook"),
	faFacebookOfficial("fa-facebook-official"),
	faFacebookSquare("fa-facebook-square"),
	faFastBackward("fa-fast-backward"),
	faFastForward("fa-fast-forward"),
	faFax("fa-fax"),
	faFemale("fa-female"),
	faFighterJet("fa-fighter-jet"),
	faFile("fa-file"),
	faFileArchiveO("fa-file-archive-o"),
	faFileAudioO("fa-file-audio-o"),
	faFileCodeO("fa-file-code-o"),
	faFileExcelO("fa-file-excel-o"),
	faFileImageO("fa-file-image-o"),
	faFileO("fa-file-o"),
	faFilePdfO("fa-file-pdf-o"),
	faFilePowerpointO("fa-file-powerpoint-o"),
	faFileText("fa-file-text"),
	faFileTextO("fa-file-text-o"),
	faFileVideoO("fa-file-video-o"),
	faFileWordO("fa-file-word-o"),
	faFilesO("fa-files-o"),
	faFilm("fa-film"),
	faFilter("fa-filter"),
	faFire("fa-fire"),
	faFireExtinguisher("fa-fire-extinguisher"),
	faFirefox("fa-firefox"),
	faFirstOrder("fa-first-order"),
	faFlag("fa-flag"),
	faFlagCheckered("fa-flag-checkered"),
	faFlagO("fa-flag-o"),
	faFlash("fa-flash"),
	faFlask("fa-flask"),
	faFlickr("fa-flickr"),
	faFloppyO("fa-floppy-o"),
	faFolder("fa-folder"),
	faFolderO("fa-folder-o"),
	faFolderOpen("fa-folder-open"),
	faFolderOpenO("fa-folder-open-o"),
	faFont("fa-font"),
	faFontAwesome("fa-font-awesome"),
	faFonticons("fa-fonticons"),
	faFortAwesome("fa-fort-awesome"),
	faForumbee("fa-forumbee"),
	faForward("fa-forward"),
	faFoursquare("fa-foursquare"),
	faFreeCodeCamp("fa-free-code-camp"),
	faFrownO("fa-frown-o"),
	faFutbolO("fa-futbol-o"),
	faGamepad("fa-gamepad"),
	faGavel("fa-gavel"),
	faGbp("fa-gbp"),
	faGenderless("fa-genderless"),
	faGetPocket("fa-get-pocket"),
	faGg("fa-gg"),
	faGgCircle("fa-gg-circle"),
	faGift("fa-gift"),
	faGit("fa-git"),
	faGitSquare("fa-git-square"),
	faGithub("fa-github"),
	faGithubAlt("fa-github-alt"),
	faGithubSquare("fa-github-square"),
	faGitlab("fa-gitlab"),
	faGlass("fa-glass"),
	faGlide("fa-glide"),
	faGlideG("fa-glide-g"),
	faGlobe("fa-globe"),
	faGoogle("fa-google"),
	faGooglePlus("fa-google-plus"),
	faGooglePlusOfficial("fa-google-plus-official"),
	faGooglePlusSquare("fa-google-plus-square"),
	faGoogleWallet("fa-google-wallet"),
	faGraduationCap("fa-graduation-cap"),
	faGratipay("fa-gratipay"),
	faGrav("fa-grav"),
	faHSquare("fa-h-square"),
	faHackerNews("fa-hacker-news"),
	faHandLizardO("fa-hand-lizard-o"),
	faHandODown("fa-hand-o-down"),
	faHandOLeft("fa-hand-o-left"),
	faHandORight("fa-hand-o-right"),
	faHandOUp("fa-hand-o-up"),
	faHandPaperO("fa-hand-paper-o"),
	faHandPeaceO("fa-hand-peace-o"),
	faHandPointerO("fa-hand-pointer-o"),
	faHandRockO("fa-hand-rock-o"),
	faHandScissorsO("fa-hand-scissors-o"),
	faHandSpockO("fa-hand-spock-o"),
	faHandshakeO("fa-handshake-o"),
	faHashtag("fa-hashtag"),
	faHddO("fa-hdd-o"),
	faHeader("fa-header"),
	faHeadphones("fa-headphones"),
	faHeart("fa-heart"),
	faHeartO("fa-heart-o"),
	faHeartbeat("fa-heartbeat"),
	faHistory("fa-history"),
	faHome("fa-home"),
	faHospitalO("fa-hospital-o"),
	faHourglass("fa-hourglass"),
	faHourglassEnd("fa-hourglass-end"),
	faHourglassHalf("fa-hourglass-half"),
	faHourglassO("fa-hourglass-o"),
	faHourglassStart("fa-hourglass-start"),
	faHouzz("fa-houzz"),
	faHtml5("fa-html5"),
	faICursor("fa-i-cursor"),
	faIdBadge("fa-id-badge"),
	faIdCard("fa-id-card"),
	faIdCardO("fa-id-card-o"),
	faIls("fa-ils"),
	faImdb("fa-imdb"),
	faInbox("fa-inbox"),
	faIndent("fa-indent"),
	faIndustry("fa-industry"),
	faInfo("fa-info"),
	faInfoCircle("fa-info-circle"),
	faInr("fa-inr"),
	faInstagram("fa-instagram"),
	faInternetExplorer("fa-internet-explorer"),
	faIoxhost("fa-ioxhost"),
	faItalic("fa-italic"),
	faJoomla("fa-joomla"),
	faJpy("fa-jpy"),
	faJsfiddle("fa-jsfiddle"),
	faKey("fa-key"),
	faKeyboardO("fa-keyboard-o"),
	faKrw("fa-krw"),
	faLanguage("fa-language"),
	faLaptop("fa-laptop"),
	faLastfm("fa-lastfm"),
	faLastfmSquare("fa-lastfm-square"),
	faLeaf("fa-leaf"),
	faLeanpub("fa-leanpub"),
	faLemonO("fa-lemon-o"),
	faLevelDown("fa-level-down"),
	faLevelUp("fa-level-up"),
	faLifeRing("fa-life-ring"),
	faLightbulbO("fa-lightbulb-o"),
	faLineChart("fa-line-chart"),
	faLink("fa-link"),
	faLinkedin("fa-linkedin"),
	faLinkedinSquare("fa-linkedin-square"),
	faLinode("fa-linode"),
	faLinux("fa-linux"),
	faList("fa-list"),
	faListAlt("fa-list-alt"),
	faListOl("fa-list-ol"),
	faListUl("fa-list-ul"),
	faLocationArrow("fa-location-arrow"),
	faLock("fa-lock"),
	faLongArrowDown("fa-long-arrow-down"),
	faLongArrowLeft("fa-long-arrow-left"),
	faLongArrowRight("fa-long-arrow-right"),
	faLongArrowUp("fa-long-arrow-up"),
	faLowVision("fa-low-vision"),
	faMagic("fa-magic"),
	faMagnet("fa-magnet"),
	faMale("fa-male"),
	faMap("fa-map"),
	faMapMarker("fa-map-marker"),
	faMapO("fa-map-o"),
	faMapPin("fa-map-pin"),
	faMapSigns("fa-map-signs"),
	faMars("fa-mars"),
	faMarsDouble("fa-mars-double"),
	faMarsStroke("fa-mars-stroke"),
	faMarsStrokeH("fa-mars-stroke-h"),
	faMarsStrokeV("fa-mars-stroke-v"),
	faMaxcdn("fa-maxcdn"),
	faMeanpath("fa-meanpath"),
	faMedium("fa-medium"),
	faMedkit("fa-medkit"),
	faMeetup("fa-meetup"),
	faMehO("fa-meh-o"),
	faMercury("fa-mercury"),
	faMicrochip("fa-microchip"),
	faMicrophone("fa-microphone"),
	faMicrophoneSlash("fa-microphone-slash"),
	faMinus("fa-minus"),
	faMinusCircle("fa-minus-circle"),
	faMinusSquare("fa-minus-square"),
	faMinusSquareO("fa-minus-square-o"),
	faMixcloud("fa-mixcloud"),
	faMobile("fa-mobile"),
	faModx("fa-modx"),
	faMoney("fa-money"),
	faMoonO("fa-moon-o"),
	faMotorcycle("fa-motorcycle"),
	faMousePointer("fa-mouse-pointer"),
	faMusic("fa-music"),
	faNeuter("fa-neuter"),
	faNewspaperO("fa-newspaper-o"),
	faObjectGroup("fa-object-group"),
	faObjectUngroup("fa-object-ungroup"),
	faOdnoklassniki("fa-odnoklassniki"),
	faOdnoklassnikiSquare("fa-odnoklassniki-square"),
	faOpencart("fa-opencart"),
	faOpenid("fa-openid"),
	faOpera("fa-opera"),
	faOptinMonster("fa-optin-monster"),
	faOutdent("fa-outdent"),
	faPagelines("fa-pagelines"),
	faPaintBrush("fa-paint-brush"),
	faPaperPlane("fa-paper-plane"),
	faPaperPlaneO("fa-paper-plane-o"),
	faPaperclip("fa-paperclip"),
	faParagraph("fa-paragraph"),
	faPause("fa-pause"),
	faPauseCircle("fa-pause-circle"),
	faPauseCircleO("fa-pause-circle-o"),
	faPaw("fa-paw"),
	faPaypal("fa-paypal"),
	faPencil("fa-pencil"),
	faPencilSquare("fa-pencil-square"),
	faPencilSquareO("fa-pencil-square-o"),
	faPercent("fa-percent"),
	faPhone("fa-phone"),
	faPhoneSquare("fa-phone-square"),
	faPictureO("fa-picture-o"),
	faPieChart("fa-pie-chart"),
	faPiedPiper("fa-pied-piper"),
	faPiedPiperAlt("fa-pied-piper-alt"),
	faPiedPiperPp("fa-pied-piper-pp"),
	faPinterest("fa-pinterest"),
	faPinterestP("fa-pinterest-p"),
	faPinterestSquare("fa-pinterest-square"),
	faPlane("fa-plane"),
	faPlay("fa-play"),
	faPlayCircle("fa-play-circle"),
	faPlayCircleO("fa-play-circle-o"),
	faPlug("fa-plug"),
	faPlus("fa-plus"),
	faPlusCircle("fa-plus-circle"),
	faPlusSquare("fa-plus-square"),
	faPlusSquareO("fa-plus-square-o"),
	faPodcast("fa-podcast"),
	faPowerOff("fa-power-off"),
	faPrint("fa-print"),
	faProductHunt("fa-product-hunt"),
	faPuzzlePiece("fa-puzzle-piece"),
	faQq("fa-qq"),
	faQrcode("fa-qrcode"),
	faQuestion("fa-question"),
	faQuestionCircle("fa-question-circle"),
	faQuestionCircleO("fa-question-circle-o"),
	faQuora("fa-quora"),
	faQuoteLeft("fa-quote-left"),
	faQuoteRight("fa-quote-right"),
	faRandom("fa-random"),
	faRavelry("fa-ravelry"),
	faRebel("fa-rebel"),
	faRecycle("fa-recycle"),
	faReddit("fa-reddit"),
	faRedditAlien("fa-reddit-alien"),
	faRedditSquare("fa-reddit-square"),
	faRefresh("fa-refresh"),
	faRegistered("fa-registered"),
	faRenren("fa-renren"),
	faRepeat("fa-repeat"),
	faReply("fa-reply"),
	faReplyAll("fa-reply-all"),
	faRetweet("fa-retweet"),
	faRoad("fa-road"),
	faRocket("fa-rocket"),
	faRss("fa-rss"),
	faRssSquare("fa-rss-square"),
	faRub("fa-rub"),
	faSafari("fa-safari"),
	faScissors("fa-scissors"),
	faScribd("fa-scribd"),
	faSearch("fa-search"),
	faSearchMinus("fa-search-minus"),
	faSearchPlus("fa-search-plus"),
	faSellsy("fa-sellsy"),
	faServer("fa-server"),
	faShare("fa-share"),
	faShareAlt("fa-share-alt"),
	faShareAltSquare("fa-share-alt-square"),
	faShareSquare("fa-share-square"),
	faShareSquareO("fa-share-square-o"),
	faShield("fa-shield"),
	faShip("fa-ship"),
	faShirtsinbulk("fa-shirtsinbulk"),
	faShoppingBag("fa-shopping-bag"),
	faShoppingBasket("fa-shopping-basket"),
	faShoppingCart("fa-shopping-cart"),
	faShower("fa-shower"),
	faSignIn("fa-sign-in"),
	faSignLanguage("fa-sign-language"),
	faSignOut("fa-sign-out"),
	faSignal("fa-signal"),
	faSimplybuilt("fa-simplybuilt"),
	faSitemap("fa-sitemap"),
	faSkyatlas("fa-skyatlas"),
	faSkype("fa-skype"),
	faSlack("fa-slack"),
	faSliders("fa-sliders"),
	faSlideshare("fa-slideshare"),
	faSmileO("fa-smile-o"),
	faSnapchat("fa-snapchat"),
	faSnapchatGhost("fa-snapchat-ghost"),
	faSnapchatSquare("fa-snapchat-square"),
	faSnowflakeO("fa-snowflake-o"),
	faSort("fa-sort"),
	faSortAlphaAsc("fa-sort-alpha-asc"),
	faSortAlphaDesc("fa-sort-alpha-desc"),
	faSortAmountAsc("fa-sort-amount-asc"),
	faSortAmountDesc("fa-sort-amount-desc"),
	faSortAsc("fa-sort-asc"),
	faSortDesc("fa-sort-desc"),
	faSortNumericAsc("fa-sort-numeric-asc"),
	faSortNumericDesc("fa-sort-numeric-desc"),
	faSoundcloud("fa-soundcloud"),
	faSpaceShuttle("fa-space-shuttle"),
	faSpinner("fa-spinner"),
	faSpoon("fa-spoon"),
	faSpotify("fa-spotify"),
	faSquare("fa-square"),
	faSquareO("fa-square-o"),
	faStackExchange("fa-stack-exchange"),
	faStackOverflow("fa-stack-overflow"),
	faStar("fa-star"),
	faStarHalf("fa-star-half"),
	faStarHalfO("fa-star-half-o"),
	faStarO("fa-star-o"),
	faSteam("fa-steam"),
	faSteamSquare("fa-steam-square"),
	faStepBackward("fa-step-backward"),
	faStepForward("fa-step-forward"),
	faStethoscope("fa-stethoscope"),
	faStickyNote("fa-sticky-note"),
	faStickyNoteO("fa-sticky-note-o"),
	faStop("fa-stop"),
	faStopCircle("fa-stop-circle"),
	faStopCircleO("fa-stop-circle-o"),
	faStreetView("fa-street-view"),
	faStrikethrough("fa-strikethrough"),
	faStumbleupon("fa-stumbleupon"),
	faStumbleuponCircle("fa-stumbleupon-circle"),
	faSubscript("fa-subscript"),
	faSubway("fa-subway"),
	faSuitcase("fa-suitcase"),
	faSunO("fa-sun-o"),
	faSuperpowers("fa-superpowers"),
	faSuperscript("fa-superscript"),
	faTable("fa-table"),
	faTablet("fa-tablet"),
	faTachometer("fa-tachometer"),
	faTag("fa-tag"),
	faTags("fa-tags"),
	faTasks("fa-tasks"),
	faTaxi("fa-taxi"),
	faTelegram("fa-telegram"),
	faTelevision("fa-television"),
	faTencentWeibo("fa-tencent-weibo"),
	faTerminal("fa-terminal"),
	faTextHeight("fa-text-height"),
	faTextWidth("fa-text-width"),
	faTh("fa-th"),
	faThLarge("fa-th-large"),
	faThList("fa-th-list"),
	faThemeisle("fa-themeisle"),
	faThermometerEmpty("fa-thermometer-empty"),
	faThermometerFull("fa-thermometer-full"),
	faThermometerHalf("fa-thermometer-half"),
	faThermometerQuarter("fa-thermometer-quarter"),
	faThermometerThreeQuarters("fa-thermometer-three-quarters"),
	faThumbTack("fa-thumb-tack"),
	faThumbsDown("fa-thumbs-down"),
	faThumbsODown("fa-thumbs-o-down"),
	faThumbsOUp("fa-thumbs-o-up"),
	faThumbsUp("fa-thumbs-up"),
	faTicket("fa-ticket"),
	faTimes("fa-times"),
	faTimesCircle("fa-times-circle"),
	faTimesCircleO("fa-times-circle-o"),
	faTint("fa-tint"),
	faToggleOff("fa-toggle-off"),
	faToggleOn("fa-toggle-on"),
	faTrademark("fa-trademark"),
	faTrain("fa-train"),
	faTransgender("fa-transgender"),
	faTransgenderAlt("fa-transgender-alt"),
	faTrash("fa-trash"),
	faTrashO("fa-trash-o"),
	faTree("fa-tree"),
	faTrello("fa-trello"),
	faTripadvisor("fa-tripadvisor"),
	faTrophy("fa-trophy"),
	faTruck("fa-truck"),
	faTry("fa-try"),
	faTty("fa-tty"),
	faTumblr("fa-tumblr"),
	faTumblrSquare("fa-tumblr-square"),
	faTwitch("fa-twitch"),
	faTwitter("fa-twitter"),
	faTwitterSquare("fa-twitter-square"),
	faUmbrella("fa-umbrella"),
	faUnderline("fa-underline"),
	faUndo("fa-undo"),
	faUniversalAccess("fa-universal-access"),
	faUniversity("fa-university"),
	faUnlock("fa-unlock"),
	faUnlockAlt("fa-unlock-alt"),
	faUpload("fa-upload"),
	faUsb("fa-usb"),
	faUsd("fa-usd"),
	faUser("fa-user"),
	faUserCircle("fa-user-circle"),
	faUserCircleO("fa-user-circle-o"),
	faUserMd("fa-user-md"),
	faUserO("fa-user-o"),
	faUserPlus("fa-user-plus"),
	faUserSecret("fa-user-secret"),
	faUserTimes("fa-user-times"),
	faUsers("fa-users"),
	faVenus("fa-venus"),
	faVenusDouble("fa-venus-double"),
	faVenusMars("fa-venus-mars"),
	faViacoin("fa-viacoin"),
	faViadeo("fa-viadeo"),
	faViadeoSquare("fa-viadeo-square"),
	faVideoCamera("fa-video-camera"),
	faVimeo("fa-vimeo"),
	faVimeoSquare("fa-vimeo-square"),
	faVine("fa-vine"),
	faVk("fa-vk"),
	faVolumeControlPhone("fa-volume-control-phone"),
	faVolumeDown("fa-volume-down"),
	faVolumeOff("fa-volume-off"),
	faVolumeUp("fa-volume-up"),
	faWeibo("fa-weibo"),
	faWeixin("fa-weixin"),
	faWhatsapp("fa-whatsapp"),
	faWheelchair("fa-wheelchair"),
	faWheelchairAlt("fa-wheelchair-alt"),
	faWifi("fa-wifi"),
	faWikipediaW("fa-wikipedia-w"),
	faWindowClose("fa-window-close"),
	faWindowCloseO("fa-window-close-o"),
	faWindowMaximize("fa-window-maximize"),
	faWindowMinimize("fa-window-minimize"),
	faWindowRestore("fa-window-restore"),
	faWindows("fa-windows"),
	faWordpress("fa-wordpress"),
	faWpbeginner("fa-wpbeginner"),
	faWpexplorer("fa-wpexplorer"),
	faWpforms("fa-wpforms"),
	faWrench("fa-wrench"),
	faXing("fa-xing"),
	faXingSquare("fa-xing-square"),
	faYCombinator("fa-y-combinator"),
	faYahoo("fa-yahoo"),
	faYelp("fa-yelp"),
	faYoast("fa-yoast"),
	faYoutube("fa-youtube"),
	faYoutubePlay("fa-youtube-play"),
	faYoutubeSquare("fa-youtube-square"),
	;

	private final String m_css;

	FaIcon(String css) {
		m_css = css;
	}

	public String getCssClassName() {
		return m_css;
	}

	public String getClasses() {
		return "fa " + m_css;
	}

	public NodeBase createNode(String cssClasses) {
		return new FontIcon(this).css(cssClasses);
	}

	public IIconRef css(String... classes) {
		return new WrappedIconRef(this, classes);
	}
}