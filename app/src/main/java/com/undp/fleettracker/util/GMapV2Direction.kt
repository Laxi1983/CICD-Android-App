package com.undp.fleettracker.util

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList


class GMapV2Direction {
    public val MODE_DRIVING = "driving"
    public val MODE_WALKING = "walking"

    fun GMapV2Direction() {}

    fun getDocumentUrl(start: LatLng, end: LatLng, mode: String?): String {
        val url = ("http://maps.googleapis.com/maps/api/directions/xml?"
                + "origin=" + start.latitude + "," + start.longitude
                + "&destination=" + end.latitude + "," + end.longitude
                + "&sensor=false&units=metric&mode=$mode")
        Log.d("url", url)
//        try {
//            val httpClient: HttpClient = DefaultHttpClient()
//            val localContext: HttpContext = BasicHttpContext()
//            val httpPost = HttpPost(url)
//            val response: HttpResponse = httpClient.execute(httpPost, localContext)
//            val `in`: InputStream = response.getEntity().getContent()
//            val builder: DocumentBuilder = DocumentBuilderFactory.newInstance()
//                .newDocumentBuilder()
//            return builder.parse(`in`)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
        return url
    }

    fun getDurationText(doc: Document): String? {
        return try {
            val nl1: NodeList = doc.getElementsByTagName("duration")
            val node1: Node = nl1.item(0)
            val nl2: NodeList = node1.getChildNodes()
            val node2: Node = nl2.item(getNodeIndex(nl2, "text"))
            Log.i("DurationText", node2.getTextContent())
            node2.getTextContent()
        } catch (e: Exception) {
            "0"
        }
    }

    fun getDurationValue(doc: Document): Int {
        return try {
            val nl1: NodeList = doc.getElementsByTagName("duration")
            val node1: Node = nl1.item(0)
            val nl2: NodeList = node1.getChildNodes()
            val node2: Node = nl2.item(getNodeIndex(nl2, "value"))
            Log.i("DurationValue", node2.getTextContent())
            node2.getTextContent().toInt()
        } catch (e: Exception) {
            -1
        }
    }

    fun getDistanceText(doc: Document): String? {
        /*
         * while (en.hasMoreElements()) { type type = (type) en.nextElement();
         *
         * }
         */
        return try {
            val nl1: NodeList
            nl1 = doc.getElementsByTagName("distance")
            val node1: Node = nl1.item(nl1.getLength() - 1)
            var nl2: NodeList? = null
            nl2 = node1.getChildNodes()
            val node2: Node = nl2.item(getNodeIndex(nl2, "value"))
            Log.d("DistanceText", node2.getTextContent())
            node2.getTextContent()
        } catch (e: Exception) {
            "-1"
        }

        /*
         * NodeList nl1; if(doc.getElementsByTagName("distance")!=null){ nl1=
         * doc.getElementsByTagName("distance");
         *
         * Node node1 = nl1.item(nl1.getLength() - 1); NodeList nl2 = null; if
         * (node1.getChildNodes() != null) { nl2 = node1.getChildNodes(); Node
         * node2 = nl2.item(getNodeIndex(nl2, "value")); Log.d("DistanceText",
         * node2.getTextContent()); return node2.getTextContent(); } else return
         * "-1";} else return "-1";
         */
    }

    fun getDistanceValue(doc: Document): Int {
        return try {
            val nl1: NodeList = doc.getElementsByTagName("distance")
            var node1: Node? = null
            node1 = nl1.item(nl1.getLength() - 1)
            val nl2: NodeList = node1.getChildNodes()
            val node2: Node = nl2.item(getNodeIndex(nl2, "value"))
            Log.i("DistanceValue", node2.getTextContent())
            node2.getTextContent().toInt()
        } catch (e: Exception) {
            -1
        }
        /*
         * NodeList nl1 = doc.getElementsByTagName("distance"); Node node1 =
         * null; if (nl1.getLength() > 0) node1 = nl1.item(nl1.getLength() - 1);
         * if (node1 != null) { NodeList nl2 = node1.getChildNodes(); Node node2
         * = nl2.item(getNodeIndex(nl2, "value")); Log.i("DistanceValue",
         * node2.getTextContent()); return
         * Integer.parseInt(node2.getTextContent()); } else return 0;
         */
    }

    fun getStartAddress(doc: Document): String? {
        return try {
            val nl1: NodeList = doc.getElementsByTagName("start_address")
            val node1: Node = nl1.item(0)
            Log.i("StartAddress", node1.getTextContent())
            node1.getTextContent()
        } catch (e: Exception) {
            "-1"
        }
    }

    fun getEndAddress(doc: Document): String? {
        return try {
            val nl1: NodeList = doc.getElementsByTagName("end_address")
            val node1: Node = nl1.item(0)
            Log.i("StartAddress", node1.getTextContent())
            node1.getTextContent()
        } catch (e: Exception) {
            "-1"
        }
    }

    fun getCopyRights(doc: Document): String? {
        return try {
            val nl1: NodeList = doc.getElementsByTagName("copyrights")
            val node1: Node = nl1.item(0)
            Log.i("CopyRights", node1.getTextContent())
            node1.getTextContent()
        } catch (e: Exception) {
            "-1"
        }
    }

    fun getDirection(doc: Document): ArrayList<LatLng>? {
        val nl1: NodeList
        var nl2: NodeList
        var nl3: NodeList
        val listGeopoints = ArrayList<LatLng>()
        nl1 = doc.getElementsByTagName("step")
        if (nl1.getLength() > 0) {
            for (i in 0 until nl1.getLength()) {
                val node1: Node = nl1.item(i)
                nl2 = node1.getChildNodes()
                var locationNode: Node = nl2
                    .item(getNodeIndex(nl2, "start_location"))
                nl3 = locationNode.getChildNodes()
                var latNode: Node = nl3.item(getNodeIndex(nl3, "lat"))
                var lat: Double = latNode.getTextContent().toDouble()
                var lngNode: Node = nl3.item(getNodeIndex(nl3, "lng"))
                var lng: Double = lngNode.getTextContent().toDouble()
                listGeopoints.add(LatLng(lat, lng))
                locationNode = nl2.item(getNodeIndex(nl2, "polyline"))
                nl3 = locationNode.getChildNodes()
                latNode = nl3.item(getNodeIndex(nl3, "points"))
                val arr = decodePoly(latNode.getTextContent())
                for (j in 0 until arr.size) {
                    listGeopoints.add(
                        LatLng(
                            arr[j].latitude, arr[j].longitude
                        )
                    )
                }
                locationNode = nl2.item(getNodeIndex(nl2, "end_location"))
                nl3 = locationNode.getChildNodes()
                latNode = nl3.item(getNodeIndex(nl3, "lat"))
                lat = latNode.getTextContent().toDouble()
                lngNode = nl3.item(getNodeIndex(nl3, "lng"))
                lng = lngNode.getTextContent().toDouble()
                listGeopoints.add(LatLng(lat, lng))
            }
        }
        return listGeopoints
    }

    private fun getNodeIndex(nl: NodeList?, nodename: String): Int {
        for (i in 0 until nl!!.length) {
            if (nl!!.item(i).getNodeName().equals(nodename)) return i
        }
        return -1
    }

    private fun decodePoly(encoded: String): ArrayList<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val position = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
            poly.add(position)
        }
        return poly
    }

    fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {

        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude

        val key = "key=AIzaSyCVsqkFTZiIm_HMi6YY2gqCV9AARYAr0cc"
        // Sensor enabled
        val sensor = "sensor=false"
        val mode = "mode=driving"

        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor&$mode&$key"

        // Output format
        val output = "json"

        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }
}