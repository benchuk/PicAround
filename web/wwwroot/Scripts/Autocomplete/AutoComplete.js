
var ready = $("document").ready(function () {

    $("#param").keyup(function () {
        _gaq.push(['_trackEvent', 'UserSearch', 'NoramlSite', '1']);
        lookup($("#param").val());
    });

    var cssObj = {
        'box-shadow': '#888 5px 10px 10px', // Added when CSS3 is standard
        '-webkit-box-shadow': '#888 5px 10px 10px', // Safari
        '-moz-box-shadow': '#888 5px 10px 10px'
    }; // Firefox 3.5+
    $("#suggestions").css(cssObj);

    $("#param").blur(function () {
        $('#suggestions').fadeOut();
    });

    var GetEventsForLocation = function (result) {
        {

            var position = $("#param").offset();
            $("#suggestions").offset({ left: position.left });
            $('#suggestions').fadeIn();
            $("#searchresults").html("");
            $.each(result, function (i, object) {
                $.each(object, function (property, value) {
                    if (property == "LocationName")
                        $("#searchresults").append("<p class=\"btn btn-primary btn-small\">" + value.LocationName + "</p>");
                    else
                        $.each(value, function (k, v) {

                            var eventLink = "/Gallery/GalleryByEventId?eventId=" + v.EventId;
                            var event = "<a href=" + eventLink + " class='searchLink'><blockquote><p><b class='searchEventName'>" + v.EventName.capitalize() + "</b></p><img class='searchImg img-polaroid' src='http://picaround.blob.core.windows.net/" + v.EventPictureLink + "'></blockquote></a>";
                            $("#searchresults").append(event);
                        });
                });
            });
            $('img').error(function () {
                $(this).attr('src', '../../Content/blue2.png');
            });
        }
    };

  

    function lookup(inputString) {
        if (inputString.length == 0) {
            $('#suggestions').fadeOut(); // Hide the suggestions box
        } else {
            $.ajax({
                url: "/Events/GetEventsForLocations",
                type: 'POST',
                traditional: true,
                dataType: "json",
                data: { 'str': inputString },
                beforeSend: function () {
                    $("#ajaxLoader").css('visibility', 'visible');
                },
                complete: function () {
                    $("#ajaxLoader").css('visibility', 'hidden');
                },
                success: function (result) {
                    $('#suggestions').fadeIn();
                    $("#searchresults").html("");
                    $.each(result, function () {
                        GetEventsForLocation(result);
                    });
                }});
        }
    }

    $("#c").click(function () {
        $.ajax({
            url: "/Events/GetEventsForLocations",
            type: 'POST',
            traditional: true,
            dataType: "json",
            data: { 'str': 'D' },
            success: function (result) {
                GetEventsForLocation(result);
            }
        });
    });
});