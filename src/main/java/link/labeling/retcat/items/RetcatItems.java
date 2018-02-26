package link.labeling.retcat.items;

import link.labeling.retcat.classes.RetcatItem;
import link.labeling.retcat.exceptions.ResourceNotAvailableException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RetcatItems {

    public static List<RetcatItem> getReferenceThesaurusCatalogue() throws IOException, ResourceNotAvailableException {
        List<RetcatItem> retcatList = new ArrayList();
        // LOCAL LABELING SYSTEM
        retcatList.add(new RetcatItem(
                "This Labeling System", //name
                "This Labeling System vocabularies.", //description
                "/v1/rtc/query/labelingsystem", //queryURL
                "/v1/rtc/info/labelingsystem", //labelURL
                "//{host}/item", //prefix
                "common reference thesauri (CH)", //group
                "ls", //type
                "n/a", //language
                "high", //quality
                true)); // is default value?
        // LABELING SYSTEM INSTANCES
        // labeling.link
        retcatList.add(new RetcatItem(
                "labeling.link", //name
                "The labeling.link vocabularies.", //description
                "/v1/rtc/query/labelinglink", //queryURL
                "/v1/rtc/info/labelinglink", //labelURL
                "//labeling.link/item", //prefix
                "common reference thesauri (CH)", //group
                "labelinglink", //type
                "n/a", //language
                "high", //quality
                true)); // is default value?
        // SKOS CONCEPTS
        // GETTY
        retcatList.add(new RetcatItem(
                "Getty AAT",
                "AAT is a structured vocabulary, including terms, descriptions, and other metadata for generic concepts related to art, architecture, conservation, archaeology, and other cultural heritage. Included are work types, styles, materials, techniques, and others.",
                "/v1/rtc/query/getty/aat",
                "/v1/rtc/info/getty",
                "//vocab.getty.edu",
                "common reference thesauri (CH)",
                "getty",
                "en",
                "high",
                true));
        retcatList.add(new RetcatItem(
                "Getty TGN",
                "TGN is a structured vocabulary, including names, descriptions, and other metadata for extant and historical cities, empires, archaeological sites, and physical features important to research of art and architecture. TGN may be linked to GIS, maps, and other geographic resources.",
                "/v1/rtc/query/getty/tgn",
                "/v1/rtc/info/getty",
                "//vocab.getty.edu",
                "common reference thesauri (CH)",
                "getty",
                "en",
                "high",
                false));
        retcatList.add(new RetcatItem(
                "Getty ULAN",
                "ULAN is a structured vocabulary, including names, biographies, related people, and other metadata about artists, architects, firms, studios, museums, patrons, sitters, and other people and groups involved in the creation and study of art and architecture.",
                "/v1/rtc/query/getty/ulan",
                "/v1/rtc/info/getty",
                "//vocab.getty.edu",
                "common reference thesauri (CH)",
                "getty",
                "en",
                "high",
                false));
        // HERITAGE DATA
        retcatList.add(new RetcatItem(
                "Heritage Data Historic England",
                "FISH Archaeological Sciences Thesaurus + FISH Building Materials Thesaurus + FISH Event Types Thesaurus + FISH Archaeological Objects Thesaurus + FISH Maritime Craft Types Thesaurus FISH Thesaurus of Monument Types + Historic England Periods Authority File + Components + Evidence.",
                "/v1/rtc/query/heritagedata/historicengland",
                "/v1/rtc/info/heritagedata",
                "//purl.org/heritagedata/schemes",
                "common reference thesauri (CH)",
                "heritagedata",
                "en",
                "high",
                true));
        retcatList.add(new RetcatItem(
                "Heritage Data RCAHMS",
                "Objects made by human activity + types of craft that survive as wrecks, or are documented as losses, in Scottish maritime waters + monument types relating to the archaeological and built heritage of Scotland. The terminology also includes Scottish Gaelic translations for some terms.",
                "/v1/rtc/query/heritagedata/rcahms",
                "/v1/rtc/info/heritagedata",
                "//purl.org/heritagedata/schemes",
                "common reference thesauri (CH)",
                "heritagedata",
                "en",
                "high",
                false));
        retcatList.add(new RetcatItem(
                "Heritage Data RCAHMW",
                "A list of periods for use in Wales + classification of monument types in Wales by function.",
                "/v1/rtc/query/heritagedata/rcahmw",
                "/v1/rtc/info/heritagedata",
                "//purl.org/heritagedata/schemes",
                "common reference thesauri (CH)",
                "heritagedata",
                "en",
                "high",
                false));
        // CHRONONTOLOGY
        retcatList.add(new RetcatItem("ChronOntology",
                "ChronOntology is a time gazetteer web service that assumes a role similar to that of place gazetteers but for temporal concepts and cultural periods as well a system for storing, managing, mapping and making accessible descriptions of temporal concepts.",
                "/v1/rtc/query/chronontology", "/v1/rtc/info/chronontology", "//chronontology.dainst.org/period", "common reference thesauri (CH)", "chronontology", "en", "high", false));
        // PLEIADES (from Pelagios Periopleo API)
        retcatList.add(new RetcatItem("Pleiades",
                "Pleiades is a community-built gazetteer and graph of ancient places. It publishes authoritative information about ancient places and spaces, providing unique services for finding, displaying, and reusing that information under open license.",
                "/v1/rtc/query/pelagiospleiadesplaces", "/v1/rtc/info/pelagiospleiadesplaces", "//pleiades.stoa.org/places", "common reference thesauri (CH)", "pleiades", "en", "high", false));
        // SKOSMOS
        retcatList.add(new RetcatItem("FINTO - Finnish Thesaurus and Ontology Service",
                "Finto is a Finnish thesaurus and ontology service, which enables both the publication and browsing of vocabularies. The service also offers interfaces for integrating the thesauri and ontologies into other applications and systems.",
                "/v1/rtc/query/skosmos/finto", "/v1/rtc/info/skosmos/finto", "//www.yso.fi", "other reference thesauri", "finto", "en", "low", false));
        retcatList.add(new RetcatItem("FAO - AGROVOC Multilingual agricultural thesaurus",
                "AGROVOC is a controlled vocabulary covering all areas of interest of the Food and Agriculture Organization (FAO) of the United Nations, including food, nutrition, agriculture, fisheries, forestry, environment etc. It is published by FAO and edited by a community of experts.",
                "/v1/rtc/query/skosmos/fao", "/v1/rtc/info/skosmos/fao", "//aims.fao.org", "other reference thesauri", "fao", "en", "low", false));
        retcatList.add(new RetcatItem("UNESCO Thesaurus",
                "The UNESCO Thesaurus is a controlled and structured list of terms used in subject analysis and retrieval of documents and publications in the fields of education, culture, natural sciences, social and human sciences, communication and information. Continuously enriched and updated, its multidisciplinary terminology reflects the evolution of UNESCO's programmes and activities.",
                "/v1/rtc/query/skosmos/unesco", "/v1/rtc/info/skosmos/unesco", "//vocabularies.unesco.org", "other reference thesauri", "unesco", "en", "high", false));
        // TEMATRES
        retcatList.add(new RetcatItem("iDAI.vocab",
                "Thesaurus of Deutsches Archäologisches Institut, called archwort. ",
                "/v1/rtc/query/archwort", "/v1/rtc/info/archwort", "//archwort.dainst.org", "other reference thesauri", "archwort", "de", "low", false));
        // MORE
        retcatList.add(new RetcatItem("British Geological Survey",
                "The British Geological Survey (BGS) is the United Kingdom's premier centre for earth science information and expertise.",
                "/v1/rtc/query/bgs", "/v1/rtc/info/bgs", "//data.bgs.ac.uk", "other reference thesauri", "bgs", "en", "low", false));
        // INTERPRET AS SKOS CONCEPT
        // DBPEDIA
        retcatList.add(new RetcatItem("DBpedia",
                "DBpedia is a project aiming to extract structured content from the information created as part of the Wikipedia project. ",
                "/v1/rtc/query/dbpedia", "/v1/rtc/info/dbpedia", "//dbpedia.org/resource", "additional information", "dbpedia", "de", "low", true));
        // WIKIDATA
        retcatList.add(new RetcatItem("Wikidata",
                "Wikidata is a free and open knowledge base that can be read and edited by both humans and machines.",
                "/v1/rtc/query/wikidata", "/v1/rtc/info/wikidata", "//www.wikidata.org/entity", "additional information", "wikidata", "en", "low", false));
        // GEONAMES
        retcatList.add(new RetcatItem("GeoNames",
                "GeoNames is a geographical database available and accessible through various web services, under a Creative Commons attribution license.",
                "/v1/rtc/query/geonames", "/v1/rtc/info/geonames", "//sws.geonames.org", "additional information", "geonames", "en", "low", false));
        return retcatList;
    }

    public static RetcatItem getRetcatItemByName(String name) throws IOException, ResourceNotAvailableException {
        List<RetcatItem> catalogue = getReferenceThesaurusCatalogue();
        for (Object item : catalogue) {
            RetcatItem tmp = (RetcatItem) item;
            if (tmp.getName().equals(name)) {
                return tmp;
            }
        }
        return null;
    }

}
