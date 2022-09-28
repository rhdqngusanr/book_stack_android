package kr.book_stack.notion

import android.util.Log
import androidx.core.util.rangeTo
import kr.book_stack.Struct
import kr.book_stack.notion.NotionAPI.toFormattedString
import org.jraf.klibnotion.model.base.UuidString
import org.jraf.klibnotion.model.base.reference.DatabaseReference
import org.jraf.klibnotion.model.base.reference.PageReference
import org.jraf.klibnotion.model.block.Block
import org.jraf.klibnotion.model.block.BulletedListItemBlock
import org.jraf.klibnotion.model.block.ChildPageBlock
import org.jraf.klibnotion.model.block.paragraph
import org.jraf.klibnotion.model.database.query.DatabaseQuery
import org.jraf.klibnotion.model.database.query.filter.DatabaseQueryPredicate
import org.jraf.klibnotion.model.database.query.filter.DatabaseQueryPropertyFilter
import org.jraf.klibnotion.model.emoji.Emoji
import org.jraf.klibnotion.model.page.Page
import org.jraf.klibnotion.model.pagination.ResultPage
import org.jraf.klibnotion.model.property.SelectOption
import org.jraf.klibnotion.model.property.spec.PropertySpecList
import org.jraf.klibnotion.model.property.value.PropertyValueList
import org.jraf.klibnotion.model.richtext.RichTextList
import org.jraf.klibnotion.model.richtext.text
import org.json.JSONObject
import java.io.File
import kotlin.collections.ArrayList
import kotlin.random.Random

//https://github.com/BoD/klibnotion/blob/master/samples/sample-jvm/src/main/kotlin/org/jraf/klibnotion/sample/Sample.kt
object NotionAPI {

    suspend fun queryUserID(
        inProperty: String,
        inValue: String
    ): NotionData.User {
        val getData: NotionData.User
        println("Filtered query results:")
        val filteredQueryResultPage: ResultPage<Page> = Struct.notionClient.databases.queryDatabase(
            Struct.dbIdUser,
            query = DatabaseQuery()
                .any(
                    DatabaseQueryPropertyFilter.Text(
                        propertyIdOrName = inProperty,
                        predicate = DatabaseQueryPredicate.Text.Equals(inValue)
                    )
                )
        )
        println(filteredQueryResultPage)
        if (filteredQueryResultPage.results.isEmpty()) {
            getData = NotionData.User(
                "empty",
                "",
                "",
                "",
                ""
            )
        } else {
            var id = ""
            var name = ""
            var userPageId = ""
            var bookPageId = ""
            var tagPageId = ""
            for (i in filteredQueryResultPage.results[0].propertyValues.indices) {
                val property = filteredQueryResultPage.results[0].propertyValues[i]
                when (property.name) {
                    "id" -> {
                        id = property.value.toFormattedString()
                    }
                    "name" -> {
                        name = property.value.toFormattedString()
                    }
                    "user_page_id" -> {
                        userPageId = property.value.toFormattedString()
                    }
                    "book_page_id" -> {
                        bookPageId = property.value.toFormattedString()
                    }
                    "tag_page_id" -> {
                        tagPageId = property.value.toFormattedString()
                    }
                }

            }
            getData = NotionData.User(
                id,
                name,
                userPageId,
                bookPageId,
                tagPageId
            )
            Log.e(
                "getData", """
                |id $id
                |name $name
                |userPageId $userPageId
                |bookPageId $bookPageId
                |tagPageId $tagPageId
            """.trimMargin()
            )
        }
        return getData
    }

    suspend fun createUserDB(inId: String, inName: String, inProfile: String): UuidString {
        println("Created page in database:")
        val createdPageInDb: Page = Struct.notionClient.pages.createPage(
            parentDatabase = DatabaseReference(Struct.dbIdUser),
            properties = PropertyValueList()
                .title("id", inId)
                .text(
                    "name", RichTextList()
                        .text(inName)
                )
                .url(
                    "profile_img", inProfile
                )
                .text(
                    "user_page_id", RichTextList()
                        .text("")
                )
                .text(
                    "book_page_id", RichTextList()
                        .text("")
                )
                .text(
                    "tag_page_id", RichTextList()
                        .text("")
                )
        )

        return createdPageInDb.id
    }

    suspend fun createBookDatabase(inId: String): UuidString {
        // Create a database
        println("Created database:")
        val createdDatabase = Struct.notionClient.databases.createDatabase(
            parentPageId = Struct.dbIdBook,
            title = text("$inId Book Table"),
            icon = Emoji("1️⃣"),
            properties = PropertySpecList()
                .title("id")
                .text("name")
                .text("page_id")
                .text("isbn")
                .text("book_status")
                .text("book_page")
                .text("look_page")
                .text("look_first")
                .text("look_last")
                .url("img")
                .text("tag")
                .text("highlight")
        )
        println(createdDatabase)
        return createdDatabase.id
    }

    suspend fun createTagDatabase(inId: String): UuidString {
        // Create a database
        println("Created database:")
        val createdDatabase = Struct.notionClient.databases.createDatabase(
            parentPageId = Struct.dbIdTag,
            title = text("$inId Tag Table"),
            icon = Emoji("2️⃣"),
            properties = PropertySpecList()
                .title("id")
                .text("page_id")
                .text("tag")
                .text("tag_icon")
        )
        println(createdDatabase)
        return createdDatabase.id
    }

    suspend fun updateUserPageId(pageId: UuidString, dbPageId: String, tagPageId: String) {
        println("Updated page:")
        val updatedPage: Page = Struct.notionClient.pages.updatePage(
            id = pageId,
            properties = PropertyValueList()
                .text("user_page_id", pageId)
                .text("book_page_id", dbPageId)
                .text("tag_page_id", tagPageId)
        )
        println(updatedPage)
    }

    suspend fun createTagPage(inId: String,inPageId:String, inTag : String, inTagImg : String): UuidString {
        println("Created page in database:")
        val createdPageInDb: Page = Struct.notionClient.pages.createPage(
            parentDatabase = DatabaseReference(inPageId),
            properties = PropertyValueList()
                .title("id", inId)
                .text(
                    "page_id", RichTextList()
                        .text("")
                )
                .text(
                    "tag", RichTextList()
                        .text(inTag)
                )
                .text(
                    "tag_icon", RichTextList()
                        .text(inTagImg)
                )
        )

        return createdPageInDb.id
    }
    suspend fun updateTagPageId(pageId: UuidString) {
        println("Updated page:")
        val updatedPage: Page = Struct.notionClient.pages.updatePage(
            id = pageId,
            properties = PropertyValueList()
                .text("page_id", pageId)

        )
        println(updatedPage)
    }
    suspend fun queryBookDatabaseFilters(
        inProperty: String,
        inValue: String
    ): ArrayList<NotionData.Book> {
        val getDataArray: ArrayList<NotionData.Book> = ArrayList()
        println("Filtered query results:")
        val filteredQueryResultPage: ResultPage<Page> = Struct.notionClient.databases.queryDatabase(
            Struct.dbIdBook,
            query = DatabaseQuery()
                .any(
                    DatabaseQueryPropertyFilter.Text(
                        propertyIdOrName = inProperty,
                        predicate = DatabaseQueryPredicate.Text.Equals(inValue)
                    )
                )
        )
        println(filteredQueryResultPage)
        for (i in filteredQueryResultPage.results.indices) {
            var email = ""
            var pageId = ""
            var isbn = ""
            var bookStatus = ""
            var bookPage = ""
            var lookPage = ""
            var lookFirst = ""
            var lookLast = ""
            var img = ""
            for (j in filteredQueryResultPage.results[i].propertyValues.indices) {
                val property = filteredQueryResultPage.results[i].propertyValues[j]
                when (property.name) {
                    "email" -> {
                        email = property.value.toFormattedString()
                    }
                    "page_id" -> {
                        pageId = property.value.toFormattedString()
                    }
                    "isbn" -> {
                        isbn = property.value.toFormattedString()
                    }
                    "book_status" -> {
                        bookStatus = property.value.toFormattedString()
                    }
                    "book_page" -> {
                        bookPage = property.value.toFormattedString()
                    }
                    "look_page" -> {
                        lookPage = property.value.toFormattedString()
                    }
                    "look_first" -> {
                        lookFirst = property.value.toFormattedString()
                    }
                    "look_last" -> {
                        lookLast = property.value.toFormattedString()
                    }
                    "img" -> {
                        img = property.value.toFormattedString()
                    }
                }

            }

            Log.e(
                "getData", """
                |email $email
                |page_id $pageId
                |isbn $isbn
                |book_status $bookStatus
                |book_page $bookPage
                |look_page $lookPage
                |look_first $lookFirst
                |look_last $lookLast
                |img $img
            """.trimMargin()
            )
            val getData: NotionData.Book = NotionData.Book(
                email,
                pageId,
                isbn,
                bookStatus,
                bookPage,
                lookPage,
                lookFirst,
                lookLast,
                img
            )
            getDataArray.add(getData)
        }


        return getDataArray
    }

    suspend fun updatePage(pageId: UuidString) {
        println("Updated page:")
        val updatedPage: Page = Struct.notionClient.pages.updatePage(
            id = pageId,
            icon = Emoji("❤️"),
            properties = PropertyValueList()
                .title("The title", "A page in a database (updated) ${Random.nextInt()}")
                .checkbox("Is checked", Random.nextBoolean())
                .text("Text 1", null)
                .number("Number 2", null)
        )
        println(updatedPage)
    }


    suspend fun createPageInPageWithContent() {
        println("Created page in page (with content):")
        val createdPageInPage: Page = Struct.notionClient.pages.createPage(
            parentPage = PageReference("7a2f2e279c1d45748ccbad8f1fd745a8"),
            title = text("A page in a page ${Random.nextInt()}"),
            icon = Emoji("⚙️")
        ) {
            paragraph("Hello, World!")
            bookmark("https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=250936100", "test")

        }
        println(createdPageInPage)
    }

    suspend fun createPageInBookDatabase(): UuidString {
        println("Created page in database:")
        val createdPageInDb: Page = Struct.notionClient.pages.createPage(
            parentDatabase = DatabaseReference(Struct.dbIdBook),
            properties = PropertyValueList()
                .title("email", "A page in a database ${Random.nextInt()}")
                .text(
                    "page_id", RichTextList()
                        .text("${Random.nextInt()} ")
                )
                .text(
                    "isbn", RichTextList()
                        .text("${Random.nextInt()} ")
                )
                .text(
                    "book_status", RichTextList()
                        .text("${Random.nextInt()} ")
                )
                .text(
                    "book_page", RichTextList()
                        .text("${Random.nextInt()} ")
                )
                .text(
                    "look_page", RichTextList()
                        .text("${Random.nextInt()} ")
                )
                .text(
                    "look_first", RichTextList()
                        .text("${Random.nextInt()} ")
                )
                .text(
                    "look_last", RichTextList()
                        .text("${Random.nextInt()} ")
                )
                .url("img", "https://zgluteks.com/${Random.nextInt()}")
            // Unsupported for now
            // .relation("Relation", ROOT_PAGE_ID)
        )

        return createdPageInDb.id
    }

    private fun <T> T.toFormattedString(): String {
        return when (this) {
            is RichTextList -> plainText ?: ""
            is SelectOption -> name
            else -> toString()
        }
    }

    fun createPageJson(): JSONObject {
        val String = """
            {
                "parent": {
                    "page_id": "7a2f2e279c1d45748ccbad8f1fd745a8"
                },
                "properties": {
                    "title": {
                        "title": [
                            {
                                "type": "text",
                                "text": {
                                    "content": " 👩앱에서 보내는겁니다"
                                }
                            }
                        ]
                    }
                },
                "children": [
                    {
                        "type": "callout",
                        "callout": {
                            "rich_text": [
                                {
                                    "type": "text",
                                    "text": {
                                        "content": "평소"
                                    }
                                },
                                {
                                    "type": "text",
                                    "text": {
                                        "content": " 마케팅"
                                    },
                                    "annotations": {
                                        "color": "red_background"
                                    }
                                },
                                {
                                    "type": "text",
                                    "text": {
                                        "content": " 디자인"
                                    },
                                    "annotations": {
                                        "color": "blue_background"
                                    }
                                },
                                {
                                    "type": "text",
                                    "text": {
                                        "content": " 분야에 관심을 가지고 "
                                    }
                                },
                                {
                                    "type": "text",
                                    "text": {
                                        "content": "총 15권"
                                    },
                                    "annotations": {
                                        "bold": true
                                    }
                                },
                                {
                                    "type": "text",
                                    "text": {
                                        "content": " 을 읽엇습니다"
                                    }
                                }
                            ],
                            "icon": {
                                "emoji": "⭐"
                            },
                            "color": "gray_background"
                        }
                    },
                    {
                        "type": "column_list",
                        "column_list": {
                            "children": [
                                {
                                    "type": "column",
                                    "column": {
                                        "children": [
                                            {
                                                "type": "callout",
                                                "callout": {
                                                    "rich_text": [
                                                        {
                                                            "type": "text",
                                                            "text": {
                                                                "content": "                                                                    테스트 1"
                                                            }
                                                        }
                                                    ],
                                                    "icon": {
                                                        "emoji": "⭐"
                                                    },
                                                    "color": "default"
                                                }
                                            },
                                            {
                                                "object": "block",
                                                "type": "paragraph",
                                                "paragraph": {
                                                    "rich_text": [
                                                        {
                                                            "type": "text",
                                                            "text": {
                                                                "content": "🏓 "
                                                            }
                                                        },
                                                        {
                                                            "type": "text",
                                                            "text": {
                                                                "content": "북스택 200% 활용하기",
                                                                "link": {
                                                                    "url": "https://www.notion.so/45899014373a421a87e989cc3cb234cc"
                                                                }
                                                            },
                                                            "annotations": {
                                                                "underline": true
                                                            }
                                                        }
                                                    ]
                                                }
                                            }
                                        ]
                                    }
                                },
                                {
                                    "type": "column",
                                    "column": {
                                        "children": [
                                            {
                                                "type": "callout",
                                                "callout": {
                                                    "rich_text": [
                                                        {
                                                            "type": "text",
                                                            "text": {
                                                                "content": "                                                                                테스트 2"
                                                            }
                                                        }
                                                    ],
                                                    "icon": {
                                                        "emoji": "⭐"
                                                    },
                                                    "color": "default"
                                                }
                                            }
                                        ]
                                    }
                                }
                            ]
                        }
                    }
                ]
            }
        """.trimIndent()



        Log.e("json", "${JSONObject(String)}")
        return JSONObject(String)
    }


}